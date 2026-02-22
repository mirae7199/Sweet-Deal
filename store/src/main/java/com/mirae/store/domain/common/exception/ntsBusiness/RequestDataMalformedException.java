package com.mirae.store.domain.common.exception.ntsBusiness;

import com.mirae.global.errorcode.ErrorCode;

public class RequestDataMalformedException extends OpenApiException {

    public RequestDataMalformedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RequestDataMalformedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
