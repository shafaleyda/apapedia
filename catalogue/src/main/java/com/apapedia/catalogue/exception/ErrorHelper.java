package com.apapedia.catalogue.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Configuration
@Log4j2
public class ErrorHelper {

	public static final String RAW_TEMPLATE = "rawTemplate";

	public ErrorHelper() {
	}

	public ResponseEntity<ErrorResponse> throwErrorException(String errorCode, HttpStatus httpStatus) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(errorCode);
		errorResponse.setTitle("Kesalahan terjadi");
		errorResponse.setMessage("Kesalahan terjadi");

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), httpStatus);
	}

	public ResponseEntity<ErrorResponse> throwErrorExceptionWithHardcodedMsg(String errorCode, HttpStatus httpStatus,
			String errorDesc, String errorMsg) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(errorCode);
		errorResponse.setTitle(errorDesc);
		errorResponse.setMessage(errorMsg);
		return new ResponseEntity<>(errorResponse, new HttpHeaders(), httpStatus);
	}

	public ResponseEntity<ErrorResponse> throwErrorExceptionWithMessage(HttpStatus httpStatus, String errorCode, String errorTitle, String errorMessage) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode(errorCode);
		errorResponse.setTitle(errorTitle);
		errorResponse.setMessage(errorMessage);

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), httpStatus);
	}
}