package com.skrypnik.javaee.service;

import com.skrypnik.javaee.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	void register(UserDto userDto);
}
