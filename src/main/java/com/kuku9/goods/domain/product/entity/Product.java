package com.kuku9.goods.domain.product.entity;

import com.kuku9.goods.domain.seller.dto.ProductRegistRequestDto;
import com.kuku9.goods.domain.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Seller seller;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int price;

    @Column
    private String status;

    public Product(ProductRegistRequestDto requestDto) {
        this.name = requestDto.getProductName();
        this.description = requestDto.getProductDescription();
        this.price = requestDto.getProductPrice();
    }
}
