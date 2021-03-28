package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.UserEntity;
import com.skrypnik.javaee.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

	private final UserService userService;

	@GetMapping
	public String registrationForm(Model model) {
		model.addAttribute("user", new UserEntity());
		return "registration";
	}

	@PostMapping
	public String register(@ModelAttribute UserEntity user) {
		userService.register(user);
		return "redirect:/";
	}

}
