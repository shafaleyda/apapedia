package com.apapedia.catalogue.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse implements Serializable {
	private String code;
	private String title;
	private String message;
}
