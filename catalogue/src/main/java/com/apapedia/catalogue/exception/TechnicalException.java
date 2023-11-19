package com.apapedia.catalogue.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class TechnicalException extends BaseException {

    public TechnicalException(String errorCode, String errorDesc, String errorMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, "", errorDesc, errorMessage);
    }

    public TechnicalException(HttpStatus httpStatus, String errorCode, String errorDesc, String errorMessage){
        super(httpStatus, errorCode, "", errorDesc, errorMessage);
    }

    public TechnicalException(String errorCode){
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, "");
    }

    public TechnicalException(HttpStatus httpStatus, String errorCode){
        super(httpStatus, errorCode, "");
    }

    public TechnicalException(String errorCode, String rootCause){
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, rootCause);
    }

    public TechnicalException(HttpStatus httpStatus, String errorCode, String rootCause) {
        super(httpStatus, errorCode, rootCause);
    }

    public TechnicalException(String errorCode, String rootCause, Map<String, String> maps) {
        super(HttpStatus.CONFLICT, errorCode, rootCause, maps);
    }

    public TechnicalException(HttpStatus httpStatus, String errorCode, String rootCause, Map<String, String> maps) {
        super(httpStatus, errorCode, rootCause, maps);
    }
}
