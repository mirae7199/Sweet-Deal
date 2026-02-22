package com.mirae.order.domain.common.exception.order;

import com.mirae.global.errorcode.ErrorCode;

public class ItemCountNotEnoughException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public ItemCountNotEnoughException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getDescription() + " — " + detailMessage);
        this.errorCode = errorCode;
        this.description = errorCode.getDescription() + "\n" + detailMessage;
    }

    public ItemCountNotEnoughException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
