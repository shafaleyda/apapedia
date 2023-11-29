package com.apapedia.catalogue.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ExtendException extends BaseException {

    public ExtendException(String errorCode, String errorDesc, String errorMessage) {
        super(HttpStatus.CONFLICT, errorCode, "", errorDesc, errorMessage);
    }

    public ExtendException(HttpStatus httpStatus, String errorCode, String errorDesc, String errorMessage) {
        super(httpStatus, errorCode, "", errorDesc, errorMessage);
    }

    public ExtendException(String errorCode) {
        super(HttpStatus.CONFLICT, errorCode, "");
    }
    public ExtendException(HttpStatus httpStatus, String errorCode) {
        super(httpStatus, errorCode, "");
    }

    public ExtendException(String errorCode, Map<String, String> maps) {
        super(HttpStatus.CONFLICT, errorCode, "", maps);
    }

    public ExtendException(HttpStatus httpStatus, String errorCode, Map<String, String> maps) {
        super(httpStatus, errorCode, "", maps);
    }

    public ExtendException(String errorCode, String rootCause) {
        super(HttpStatus.CONFLICT, errorCode, rootCause);
    }

    public ExtendException(HttpStatus httpStatus, String errorCode, String rootCause) {
        super(httpStatus, errorCode, rootCause);
    }

    public ExtendException(String errorCode, String rootCause, Map<String, String> maps) {
        super(HttpStatus.CONFLICT, errorCode, rootCause, maps);
    }

    public ExtendException(HttpStatus httpStatus, String errorCode, String rootCause, Map<String, String> maps) {
        super(httpStatus, errorCode, rootCause, maps);
    }
}
