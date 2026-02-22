package com.mirae.store.domain.store.repository.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ImageKind {


  ITEM("상품 이미지"),
  STORE("스토어 이미지");

  private String description;

}
