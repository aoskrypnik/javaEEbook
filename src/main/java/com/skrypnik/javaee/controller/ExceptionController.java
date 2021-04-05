package com.skrypnik.javaee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static com.skrypnik.javaee.util.ControllerUtils.getErrorsMap;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException exception) {
		return ResponseEntity
				.badRequest()
				.body(getErrorsMap(exception.getBindingResult()));
	}
}
