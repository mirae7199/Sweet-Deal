package com.example.store.domain.store.controller.model.request;

import com.example.store.domain.store.repository.enums.ImageKind;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateImageRequest(
    Long itemId,
    Long storeId,
    ImageKind imageKind,
    List<String> serverName
  ) {

  public static UpdateImageRequest of(
      Long itemId,
      Long storeId,
      ImageKind imageKind,
      List<String> serverName
  ) {
    return UpdateImageRequest.builder()
        .itemId(itemId)
        .storeId(storeId)
        .imageKind(imageKind)
        .serverName(serverName)
        .build();
  }


}