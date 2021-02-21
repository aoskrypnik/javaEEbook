package com.skrypnik.javaee.service;

import com.skrypnik.javaee.model.Book;

import java.util.List;

public interface BookService {

	Book save(Book book);

	List<Book> get(String searchString);
}
