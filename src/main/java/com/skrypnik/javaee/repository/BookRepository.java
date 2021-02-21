package com.skrypnik.javaee.repository;

import com.skrypnik.javaee.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Repository
@Slf4j
public class BookRepository {

	private static final Map<String, Book> BOOK_DATABASE = initDataBase();

	private static Map<String, Book> initDataBase() {
		Map<String, Book> database = new HashMap<>();
		database.put("1234567899999", new Book("1234567899999", "Chorna rada", "Panteleymon Kulish"));
		database.put("1234567888888", new Book("1234567888888", "Zapovit", "Taras Shevchenko"));
		return database;
	}

	public Book save(Book book) {
		log.info("Saving book {}", book.getIsbn());
		BOOK_DATABASE.put(book.getIsbn(), book);
		return book;
	}

	public Book findByIsbn(String isbn) {
		return BOOK_DATABASE.get(isbn);
	}

	public List<Book> findAll() {
		return new ArrayList<>(BOOK_DATABASE.values());
	}

	// Here because it can be sql query potentially
	public List<Book> findByIsbnOrTitleContains(String string) {
		return BOOK_DATABASE.values()
				.stream()
				.filter(book -> book.getIsbn().contains(string) || book.getTitle().contains(string))
				.collect(toList());
	}
}
