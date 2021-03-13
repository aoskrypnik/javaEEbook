package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

	private final BookService bookService;

	@GetMapping
	public Page<Book> search(@RequestParam(required = false, defaultValue = "") String searchString,
							 @SortDefault(sort = "title") Pageable pageable) {
		return bookService.get(searchString, pageable);
	}

	@PostMapping
	public Book create(@RequestBody Book book) {
		return bookService.save(book);
	}
}
