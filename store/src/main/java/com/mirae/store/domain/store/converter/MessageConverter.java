package com.mirae.store.domain.store.converter;

import com.mirae.global.anntation.Converter;
import com.mirae.store.domain.store.controller.model.response.MessageResponse;

@Converter
public class MessageConverter {
  public MessageResponse toResponse(String message) {
    return MessageResponse.builder()
        .message(message)
        .build();
  }

}
