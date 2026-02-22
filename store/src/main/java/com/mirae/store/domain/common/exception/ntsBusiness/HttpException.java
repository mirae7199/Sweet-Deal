package com.mirae.store.domain.common.exception.ntsBusiness;

import com.mirae.global.errorcode.ErrorCode;

public class HttpException extends OpenApiException {
    public HttpException(ErrorCode errorCode) {
        super(errorCode);
    }

    public HttpException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
