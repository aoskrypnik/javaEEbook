package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.dto.UserDto;
import com.skrypnik.javaee.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static com.skrypnik.javaee.util.ControllerUtils.populateModelWithErrorsMap;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

	private final UserService userService;

	@GetMapping
	public String registrationForm(Model model) {
		model.addAttribute("user", new UserDto());
		return "registration";
	}

	@PostMapping
	public String register(@Valid UserDto user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			populateModelWithErrorsMap(bindingResult, model);
			model.addAttribute("user", user);
			return "registration";
		}

		userService.register(user);
		return "redirect:/";
	}

}
