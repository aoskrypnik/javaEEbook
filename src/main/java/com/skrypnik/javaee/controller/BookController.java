package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping
	public String indexPage(Model model) {
		model.addAttribute("book", new Book());
		return "index";
	}

	@GetMapping(value = "/books/{isbn}")
	public String bookPage(@PathVariable String isbn, Model model) {
		model.addAttribute("book", bookService.getByIsbn(isbn));
		return "book";
	}
}
