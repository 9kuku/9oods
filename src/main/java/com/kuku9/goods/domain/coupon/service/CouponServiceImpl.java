package com.kuku9.goods.domain.coupon.service;

import static com.kuku9.goods.global.exception.ExceptionStatus.*;

import com.kuku9.goods.domain.coupon.dto.CouponRequest;
import com.kuku9.goods.domain.coupon.dto.CouponResponse;
import com.kuku9.goods.domain.coupon.entity.Coupon;
import com.kuku9.goods.domain.coupon.repository.CouponQuery;
import com.kuku9.goods.domain.coupon.repository.CouponRepository;
import com.kuku9.goods.domain.event.repository.EventQuery;
import com.kuku9.goods.domain.issued_coupon.repository.IssuedCouponQuery;
import com.kuku9.goods.domain.issued_coupon.repository.IssuedCouponRepository;
import com.kuku9.goods.domain.user.entity.User;
import com.kuku9.goods.global.concurrencycontrol.DistributedLock;
import com.kuku9.goods.global.event.IssueEvent;
import com.kuku9.goods.global.exception.InvalidCouponException;
import com.kuku9.goods.global.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final IssuedCouponQuery issuedCouponQuery;
    private final CouponRepository couponRepository;
    private final EventQuery eventQuery;
    private final IssuedCouponRepository issuedCouponRepository;
    private final ApplicationEventPublisher publisher;
    private final CouponQuery couponQuery;

    @Transactional
    public Long createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon(request);
        Coupon savedCoupon = couponRepository.save(coupon);
        return savedCoupon.getId();
    }

    @Transactional(readOnly = true)
    public CouponResponse getCoupon(Long couponId) {
        Coupon coupon = findCoupon(couponId);
        return CouponResponse.from(coupon);
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        couponRepository.delete(findCoupon(couponId));
    }

    private Coupon findCoupon(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @DistributedLock(
        lockName = "dsCouponLock",
        key = "#couponId",
        waitTime = 40,
        leaseTime = 60)
    public void issueCouponFromEvent(
        Long couponId, User user,
        LocalDateTime now) {
        boolean isDuplicatedIssuance = issuedCouponRepository.existsByCouponIdAndUserId(
            couponId, user.getId());
        if (isDuplicatedIssuance) {
            throw new InvalidCouponException(DUPLICATED_COUPON);
        }

        LocalDateTime openAt = eventQuery.getOpenDate(couponId);
        if (now.isBefore(openAt)) {
            throw new InvalidCouponException(BEFORE_EVENT_OPEN);
        }

        Coupon coupon = findCoupon(couponId);
        if (coupon.getQuantity() <= 0) {
            throw new InvalidCouponException(OUT_OF_STOCK_COUPON);
        }
        coupon.decrease();
        couponRepository.save(coupon);

        publisher.publishEvent(new IssueEvent(coupon, user));
        log.info("쿠폰 재고: {}", coupon.getQuantity());
    }

    @DistributedLock(
        lockName = "lock",
        key = "#suCoupon",
        waitTime = 40,
        leaseTime = 60)
    public void issueCoupon(User user) {
        List<Coupon> suCouponIds = couponQuery.findByCategory("su");
        for (Coupon coupon : suCouponIds) {
            boolean isDuplicatedIssuance = issuedCouponRepository.existsByCouponIdAndUserId(
                coupon.getId(), user.getId());
            if (isDuplicatedIssuance) {
                throw new InvalidCouponException(INVALID_COUPON);
            }
            if (coupon.getQuantity() <= 0) {
                log.info(
                    String.format("회원가입 쿠폰 수량 부족 [쿠폰 ID : %s | 유저 ID : %s]",
                        coupon.getId(), user.getId())
                );
            } else {
                coupon.decrease();
                couponRepository.save(coupon);

                publisher.publishEvent(new IssueEvent(coupon, user));
            }
        }
    }

    @Transactional
    public void useCoupon(Long issuedCouponId) {

        issuedCouponQuery.deleteCoupon(issuedCouponId);
    }
}
