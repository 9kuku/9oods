package com.kuku9.goods.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSearchResponse {

    private String productName;
    private String introduce;
    private int price;

}
