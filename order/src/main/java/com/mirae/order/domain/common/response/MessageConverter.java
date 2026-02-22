package com.mirae.order.domain.common.response;

import com.mirae.global.anntation.Converter;

@Converter
public class MessageConverter {
  public MessageResponse toResponse(String message) {
    return MessageResponse.builder()
        .message(message)
        .build();

  }

}
