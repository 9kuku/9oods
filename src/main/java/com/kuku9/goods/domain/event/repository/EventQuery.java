package com.kuku9.goods.domain.event.repository;

import com.kuku9.goods.domain.event.dto.*;
import java.util.*;

public interface EventQuery {

    List<ProductInfo> getEventProductInfo(Long eventId);

    List<EventTitleResponse> getEventTitles();

    void deleteEventProduct(Long eventId);

}
