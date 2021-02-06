package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@GetMapping
	public String indexPage(Model model) {
		model.addAttribute("books", bookService.getAll());
		return "index";
	}

	@GetMapping("/addBook")
	public String addBookPage(Model model) {
		model.addAttribute("book", new Book());
		return "addBook";
	}

	@PostMapping("/addBook")
	public String addBook(@ModelAttribute Book book) {
		bookService.save(book);
		return "redirect:/";
	}
}
