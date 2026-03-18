package com.example.store.domain.store.controller.model.response;

import java.util.List;
import lombok.Builder;

@Builder
public record StoreSimpleResponse(
    List<Long> storesId
  ) {

}
