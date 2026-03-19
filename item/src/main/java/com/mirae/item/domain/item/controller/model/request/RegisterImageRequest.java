package com.mirae.item.domain.item.controller.model.request;

import com.mirae.item.domain.item.entity.enums.ImageKind;
import java.util.List;
import lombok.Builder;

@Builder
public record RegisterImageRequest(
    Long itemId,
    Long storeId,
    ImageKind imageKind,
    List<String> serverName
) {

  public static RegisterImageRequest of(
      Long itemId,
      Long storeId,
      ImageKind imageKind,
      List<String> serverName
  ) {
    return RegisterImageRequest.builder()
        .itemId(itemId)
        .storeId(storeId)
        .imageKind(imageKind)
        .serverName(serverName)
        .build();
  }

}
