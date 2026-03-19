package com.mirae.item.domain.item.controller.model.request;


import com.mirae.item.domain.item.entity.enums.ImageKind;
import java.util.List;
import lombok.Builder;


@Builder
public record UpdateImageRequest(
    Long itemId,
    Long storeId,
    ImageKind imageKind,
    List<String> serverNames
) {

  public static UpdateImageRequest of(
      Long itemId,
      Long storeId,
      ImageKind imageKind,
      List<String> serverNames
  ) {
    return UpdateImageRequest.builder()
        .itemId(itemId)
        .storeId(storeId)
        .imageKind(imageKind)
        .serverNames(serverNames)
        .build();
  }


}
