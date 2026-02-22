package com.mirae.order.domain.common.exception.order;

import com.mirae.global.errorcode.ErrorCode;

public class ExpiredException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public ExpiredException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }


}
