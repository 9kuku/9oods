package com.kuku9.goods.domain.order.controller;

import com.kuku9.goods.domain.order.dto.OrderResponse;
import com.kuku9.goods.domain.order.dto.OrdersRequest;
import com.kuku9.goods.domain.order.entity.Order;
import com.kuku9.goods.domain.order.service.OrderService;
import com.kuku9.goods.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService productOrderService;

    @Operation(summary = "주문 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_USER')")
    public ResponseEntity<String> createOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody OrdersRequest productOrderRequest) {
        Order order = productOrderService.createOrder(userDetails.getUser(),
            productOrderRequest);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + order.getId())).build();
    }

    @Operation(summary = "주문 단건 조회")
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_USER')")
    public ResponseEntity<OrderResponse> getOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId)
        throws AccessDeniedException {
        OrderResponse productOrder = productOrderService.getOrder(userDetails.getUser(),
            orderId);
        return ResponseEntity.ok(productOrder);
    }

    @Operation(summary = "주문 전체 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_USER')")
    public ResponseEntity<Page<OrderResponse>> getAllOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> productOrders = productOrderService.getAllOrder(userDetails.getUser(),
            pageable);
        return ResponseEntity.ok(productOrders);
    }

    @Operation(summary = "주문 수정")
    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_USER')")
    public ResponseEntity<OrderResponse> updateOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId)
        throws AccessDeniedException {
        OrderResponse productOrder = productOrderService.updateOrder(userDetails.getUser(),
            orderId);
        return ResponseEntity.ok(productOrder);
    }

    @Operation(summary = "주문 삭제")
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<Void> deleteOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long orderId) throws AccessDeniedException {
        productOrderService.deleteOrder(userDetails.getUser(), orderId);
        return ResponseEntity.noContent().build();
    }
}
