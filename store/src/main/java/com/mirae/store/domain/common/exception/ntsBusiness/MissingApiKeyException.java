package com.mirae.store.domain.common.exception.ntsBusiness;

import com.mirae.global.errorcode.ErrorCode;

public class MissingApiKeyException extends OpenApiException {

    public MissingApiKeyException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MissingApiKeyException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }


}
