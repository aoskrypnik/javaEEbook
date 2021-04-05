package com.skrypnik.javaee.dto;

import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;

@Data
public class BookSaveDto {
	@ISBN
	private String isbn;
	@NotBlank
	private String title;
	@NotBlank
	private String author;
}
