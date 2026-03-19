package com.mirae.item.domain.item.controller.model.request;

import java.util.List;
import lombok.Builder;

@Builder
public record MessageUpdateRequest(
    Long orderId,
    List<OrderItemRequest> orderItemRequests
) {

}
