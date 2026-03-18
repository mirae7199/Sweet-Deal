package com.example.global.exception.response;

import com.example.global.errorcode.ErrorCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {

  private final LocalDateTime timestamp = LocalDateTime.now();
  private final int status;
  private final String error;
  private final String code;
  private final String message;

  // 편의 메서드 1: ErrorCode를 받아서 ResponseEntity 생성
  public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getHttpCode())
        .body(ErrorResponse.builder()
            .status(errorCode.getHttpCode())
            .error(errorCode.getHttpCode().toString())
            .code(errorCode.getErrorCode().toString())
            .message(errorCode.getDescription())
            .build()
        );
  }

  // 편의 메서드 2: 검증 에러(BindingResult) 등 구체적인 메시지가 필요할 때
  public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String customMessage) {
    return ResponseEntity
        .status(errorCode.getHttpCode())
        .body(ErrorResponse.builder()
            .status(errorCode.getHttpCode())
            .error(errorCode.getHttpCode().toString())
            .code(errorCode.getErrorCode().toString())
            .message(errorCode.getDescription())
            .build()
        );
  }
}