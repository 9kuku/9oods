package com.kuku9.goods.order;

import static com.kuku9.goods.common.TestValue.TEST_ISSUED_COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.kuku9.goods.common.TestValue;
import com.kuku9.goods.domain.issued_coupon.repository.IssuedCouponRepository;
import com.kuku9.goods.domain.order.dto.OrdersRequest;
import com.kuku9.goods.domain.order.entity.Order;
import com.kuku9.goods.domain.order.repository.OrderRepository;
import com.kuku9.goods.domain.order.service.OrderServiceImpl;
import com.kuku9.goods.domain.order_product.repository.OrderProductRepository;
import com.kuku9.goods.domain.product.entity.Product;
import com.kuku9.goods.domain.product.repository.ProductRepository;
import com.kuku9.goods.domain.user.entity.User;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private IssuedCouponRepository issuedCouponRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private OrdersRequest testOrdersRequest;
    private Product testProduct;

    @BeforeEach
    public void setup() {
        testUser = TestValue.TEST_USER1;
        testOrdersRequest = TestValue.TEST_ORDERS_REQUEST;
        testProduct = TestValue.TEST_PRODUCT;
    }

    @Test
    @DisplayName("createOrder_Success")
    public void createOrder_Success() {
        // Given
        RLock mockLock = mock(RLock.class);
        given(redissonClient.getLock(anyString())).willReturn(mockLock);
        given(productRepository.findAllById(anyList())).willReturn(
            Collections.singletonList(testProduct));
        given(orderRepository.save(any())).willReturn(
            new Order(testUser, testOrdersRequest.getAddress()));
        given(issuedCouponRepository.findById(any())).willReturn(Optional.of(TEST_ISSUED_COUPON));

        // When
        Order createdOrder = orderService.createOrder(testUser, testOrdersRequest);

        // Then
        assertNotNull(createdOrder);
        assertThat(createdOrder.getUser()).isEqualTo(testUser);
        assertThat(createdOrder.getAddress()).isEqualTo(testOrdersRequest.getAddress());
    }

    @Test
    @DisplayName("createOrder_Failure")
    public void createOrder_Failure() {
        // Given
        RLock mockLock = mock(RLock.class);
        given(redissonClient.getLock(anyString())).willReturn(mockLock);
        given(productRepository.findAllById(anyList())).willReturn(
            Collections.emptyList()); // 존재하지 않는 상품 리스트를 반환

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(testUser, testOrdersRequest);
        });
    }

}
