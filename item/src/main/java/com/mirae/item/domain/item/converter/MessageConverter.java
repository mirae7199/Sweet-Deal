package com.mirae.item.domain.item.converter;

import com.mirae.global.anntation.Converter;
import com.mirae.item.domain.item.controller.model.response.MessageResponse;

@Converter
public class MessageConverter {
  public MessageResponse toResponse(String message) {
    return MessageResponse.builder()
        .message(message)
        .build();

  }

}
