package com.skrypnik.javaee.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public final class ControllerUtils {
	private ControllerUtils() {
	}

	public static void populateModelWithErrorsMap(BindingResult bindingResult, Model model) {
		model.mergeAttributes(getErrorsMap(bindingResult));
	}

	public static Map<String, String> getErrorsMap(BindingResult bindingResult) {
		return bindingResult.getFieldErrors().stream()
				.collect(Collectors.toMap(
						fieldError -> fieldError.getField() + "Error",
						DefaultMessageSourceResolvable::getDefaultMessage
						)
				);
	}
}
