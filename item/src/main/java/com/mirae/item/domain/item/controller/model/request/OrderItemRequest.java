package com.mirae.item.domain.item.controller.model.request;

import lombok.Builder;

@Builder
public record OrderItemRequest(
    Long itemId,
    Integer quantity
) {


}
