package com.skrypnik.javaee.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDto {
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "username must have only latin letters or digits")
	private String username;
	@Size(min = 8, max = 20, message = "password length must be from 8 to 20 symbols")
	private String password;
}
