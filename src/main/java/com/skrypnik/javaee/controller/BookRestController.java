package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.dto.BookDto;
import com.skrypnik.javaee.dto.BookIsbnDto;
import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	public Page<BookDto> search(@RequestParam(required = false, defaultValue = "") String searchString,
								@SortDefault(sort = "title") Pageable pageable, Authentication authentication) {
		return bookService.search(searchString, pageable, authentication.getName());
	}

	@PostMapping
	public Book create(@RequestBody Book book) {
		return bookService.save(book);
	}

	@PostMapping("/add-favourite")
	public ResponseEntity<Void> addToFavourite(@RequestBody BookIsbnDto isbnDto, Authentication authentication) {
		bookService.addToFavourite(isbnDto.getIsbn(), authentication.getName());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/remove-favourite")
	public ResponseEntity<Void> removeFromFavourite(@RequestBody BookIsbnDto isbnDto, Authentication authentication) {
		bookService.removeFromFavourite(isbnDto.getIsbn(), authentication.getName());
		return ResponseEntity.ok().build();
	}
}
