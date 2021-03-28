package com.skrypnik.javaee.service;

import com.skrypnik.javaee.model.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	void register(UserEntity userEntity);
}
