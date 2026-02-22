package com.mirae.store.domain.common.exception.ntsBusiness;

import com.mirae.global.errorcode.ErrorCode;

public class InternalException extends OpenApiException {
    public InternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
