package com.skrypnik.javaee.service;

import com.skrypnik.javaee.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

	Book save(Book book);

	Book getByIsbn(String isbn);

	Page<Book> get(String searchString, Pageable pageable);
}
