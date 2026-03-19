package com.mirae.item.domain.item.controller.model.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ItemsResponse(
  List<ItemResponse> itemResponses
) {

}
