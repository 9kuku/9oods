package com.kuku9.goods.domain.event_product.dto;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class EventProductResponse {

    Long eventId;
    String productName;
    String productDescription;
    int productPrice;
    String domainName;

}
