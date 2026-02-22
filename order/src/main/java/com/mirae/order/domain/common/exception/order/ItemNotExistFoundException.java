package com.mirae.order.domain.common.exception.order;

import com.mirae.global.errorcode.ErrorCode;

public class ItemNotExistFoundException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public ItemNotExistFoundException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }


}
