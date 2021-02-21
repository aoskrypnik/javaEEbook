package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BookController {

	@GetMapping
	public String indexPage(Model model) {
		model.addAttribute("book", new Book());
		return "index";
	}
}
