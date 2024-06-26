package com.kuku9.goods.domain.product.controller;

import com.kuku9.goods.domain.product.dto.ProductResponse;
import com.kuku9.goods.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 단건 조회")
    @GetMapping("/sellers/{domainName}/products/{productId}")
    public ResponseEntity<ProductResponse> getProduct(
        @PathVariable Long productId, @PathVariable String domainName) {
        return ResponseEntity.ok().body(productService.getProduct(productId, domainName));
    }

    @Operation(summary = "상품 전체 조회")
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponse>> getAllProduct(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productResponses = productService.getAllProduct(pageable);
        return ResponseEntity.ok().body(productResponses);
    }

    @Operation(summary = "상품 셀러별 조회")
    @GetMapping("/sellers/{domainName}/products")
    public ResponseEntity<Page<ProductResponse>> getSellerProduct(
        @PathVariable String domainName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productResponses = productService.getSellerProduct(domainName,
            pageable);
        return ResponseEntity.ok()
            .body(productResponses);
    }

}
