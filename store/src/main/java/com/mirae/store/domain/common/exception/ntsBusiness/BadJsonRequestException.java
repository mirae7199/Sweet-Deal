package com.mirae.store.domain.common.exception.ntsBusiness;

import com.mirae.global.errorcode.ErrorCode;

public class BadJsonRequestException extends OpenApiException {

    public BadJsonRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadJsonRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
