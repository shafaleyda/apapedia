package com.apapedia.catalogue.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BusinessException extends BaseException {

    public BusinessException(String errorCode, String errorDesc, String errorMessage) {
        super(HttpStatus.CONFLICT, errorCode, "", errorDesc, errorMessage);
    }

    public BusinessException(HttpStatus httpStatus, String errorCode, String errorDesc, String errorMessage) {
        super(httpStatus, errorCode, "", errorDesc, errorMessage);
    }

    public BusinessException(String errorCode) {
        super(HttpStatus.CONFLICT, errorCode, "");
    }
    public BusinessException(HttpStatus httpStatus, String errorCode) {
        super(httpStatus, errorCode, "");
    }

    public BusinessException(String errorCode, Map<String, String> maps) {
        super(HttpStatus.CONFLICT, errorCode, "", maps);
    }

    public BusinessException(HttpStatus httpStatus, String errorCode, Map<String, String> maps) {
        super(httpStatus, errorCode, "", maps);
    }

    public BusinessException(String errorCode, String rootCause) {
        super(HttpStatus.CONFLICT, errorCode, rootCause);
    }

    public BusinessException(HttpStatus httpStatus, String errorCode, String rootCause) {
        super(httpStatus, errorCode, rootCause);
    }

    public BusinessException(String errorCode, String rootCause, Map<String, String> maps) {
        super(HttpStatus.CONFLICT, errorCode, rootCause, maps);
    }

    public BusinessException(HttpStatus httpStatus, String errorCode, String rootCause, Map<String, String> maps) {
        super(httpStatus, errorCode, rootCause, maps);
    }
}
