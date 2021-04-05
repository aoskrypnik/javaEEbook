package com.skrypnik.javaee.service.impl;

import com.skrypnik.javaee.dto.UserDto;
import com.skrypnik.javaee.exception.ModelAlreadyExistsException;
import com.skrypnik.javaee.model.UserEntity;
import com.skrypnik.javaee.repository.UserRepository;
import com.skrypnik.javaee.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

	private static final String ROLE_STUB = "AUTHENTICATED_USER";

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.map(this::buildUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
	}

	@Override
	public void register(UserDto userDto) {
		if (userRepository.findByUsername(userDto.getUsername()).isEmpty()) {
			UserEntity userEntity = UserEntity.builder()
					.username(userDto.getUsername())
					.password(passwordEncoder.encode(userDto.getPassword()))
					.build();
			userRepository.save(userEntity);
		} else {
			throw new ModelAlreadyExistsException("User already exists " + userDto.getUsername());
		}
	}

	private UserDetails buildUserDetails(UserEntity userEntity) {
		return User.builder()
				.username(userEntity.getUsername())
				.password(userEntity.getPassword())
				.authorities(ROLE_STUB)
				.build();
	}

	@PostConstruct
	private void initDatabase() {
		userRepository.save(UserEntity.builder()
				.username("andrew")
				.password(passwordEncoder.encode("andrew"))
				.build()
		);
	}
}
