package com.mirae.item.domain.item.controller.model.request;

import java.util.List;

public record MessageCancelRequest(
    Long orderId,
    List<OrderItemRequest> orderItemRequests
) {

}
