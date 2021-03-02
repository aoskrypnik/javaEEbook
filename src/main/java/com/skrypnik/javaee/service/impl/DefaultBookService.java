package com.skrypnik.javaee.service.impl;

import com.skrypnik.javaee.exception.ModelAlreadyExistsException;
import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.repository.BookRepository;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class DefaultBookService implements BookService {

	private static final String BOOK_ALREADY_EXISTS_MESSAGE = "Book %s already exists";

	private final BookRepository bookRepository;

	@Override
	@Transactional
	public Book save(Book book) {
		if (nonNull(bookRepository.findByIsbn(book.getIsbn()))) {
			throw new ModelAlreadyExistsException(String.format(BOOK_ALREADY_EXISTS_MESSAGE, book.getIsbn()));
		}
		return bookRepository.save(book);
	}

	@Override
	@Transactional
	public Book getByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Override
	@Transactional
	public List<Book> get(String searchString) {
		return searchString == null || searchString.equals("")
				? bookRepository.findAll()
				: bookRepository.findByIsbnOrTitleOrAuthorContains(searchString);
	}

	@PostConstruct
	private void initDataBase() {
		bookRepository.save(new Book("1234567899999", "Chorna rada", "Panteleymon Kulish"));
		bookRepository.save(new Book("1234567888888", "Zapovit", "Taras Shevchenko"));
	}
}
