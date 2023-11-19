package com.apapedia.catalogue.exception;

import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private ErrorHelper errorHelper;

    public GlobalExceptionHandler(ErrorHelper errorHelper) {
        this.errorHelper = errorHelper;
    }

    private Boolean isError = false;
    private static final String DEFAULT_ERROR_CODE = "80000";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> renderDefaultResponse(BaseException ex) throws JsonProcessingException {
        log.error("Exception occurred: ", ex);
        try {
            return errorHelper.throwErrorExceptionWithHardcodedMsg(ex.getErrorCode(),
                    HttpStatus.CONFLICT,
                    ex.getErrorDesc(),
                    ex.getErrorMessage());
        } catch (Exception exception) {
            log.info("failed mapper");
        }

        return errorHelper.throwErrorException(DEFAULT_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> renderMethodArgumentErrorResponse(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException occurred: ", exception);

        List<String> errors = new ArrayList<String>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
        	errors.add(error.getField().concat(":").concat(error.getDefaultMessage()));
        }

        return errorHelper.throwErrorExceptionWithMessage(HttpStatus.BAD_REQUEST, "80400", "Request Validation Error", errors.toString());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> renderHttpServerErrorResponse(HttpServerErrorException exception) {
        log.error("HttpServerErrorException occurred: ", exception);

        return errorHelper.throwErrorException(DEFAULT_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> renderTimeoutResponse(TimeoutException exception) {
        log.error("TimeoutException occurred: ", exception);

        return errorHelper.throwErrorException("80001", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Boolean isError() {
        return isError;
    }
}