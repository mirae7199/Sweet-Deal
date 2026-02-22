package com.mirae.global.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode{
    public HttpStatus getHttpStatus();
    public Integer getErrorCode();
    public String getDescription();

}
