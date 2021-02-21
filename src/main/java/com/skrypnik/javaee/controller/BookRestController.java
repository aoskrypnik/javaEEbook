package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

	private final BookService bookService;

	@GetMapping
	public List<Book> search(@RequestParam(required = false) String searchString) {
		return bookService.get(searchString);
	}

	@PostMapping
	public Book create(@RequestBody Book book) {
		return bookService.save(book);
	}
}
