package com.skrypnik.javaee.service;

import com.skrypnik.javaee.dto.BookDto;
import com.skrypnik.javaee.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

	Book save(Book book);

	Book getByIsbn(String isbn);

	Page<BookDto> search(String searchString, Pageable pageable, String username);

	List<BookDto> getFavouriteByUsername(String username);

	void addToFavourite(String isbn, String username);

	void removeFromFavourite(String isbn, String username);
}
