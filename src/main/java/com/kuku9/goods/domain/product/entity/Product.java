package com.kuku9.goods.domain.product.entity;

import com.kuku9.goods.domain.seller.dto.request.ProductRegistRequest;
import com.kuku9.goods.domain.seller.entity.Seller;
import com.kuku9.goods.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted_at=CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
@DynamicUpdate
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int price;

    @Column
    private Boolean status = true;

    @Column
    private int quantity;

    public Product(ProductRegistRequest requestDto, Seller seller) {
        this.name = requestDto.getProductName();
        this.description = requestDto.getProductDescription();
        this.price = requestDto.getProductPrice();
        this.seller = seller;
        this.quantity = requestDto.getProductQuantity();
    }

    public void updateOrderStatus(Boolean status) {
        if (status) {
            this.status = false;
        }
    }

    public void updateProduct(
        String productName, String productDescription,
        int price, int quantity) {
        this.name = productName;
        this.description = productDescription;
        this.price = price;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity -= quantity;
    }
}
