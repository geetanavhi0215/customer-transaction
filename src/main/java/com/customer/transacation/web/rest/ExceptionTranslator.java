package com.customer.transacation.web.rest;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.customer.transacation.domain.ErrorResponse;
import com.customer.transaction.errors.CustomerTransactionException;

@RestControllerAdvice
public class ExceptionTranslator {

	private static final String ERR_VALIDATION = "ERROR_VALIDATION";

	@ExceptionHandler(CustomerTransactionException.class)
	public ResponseEntity<?> handleProductNotFoundException(CustomerTransactionException exception) {
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), exception.getMessage(), ERR_VALIDATION);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

}
