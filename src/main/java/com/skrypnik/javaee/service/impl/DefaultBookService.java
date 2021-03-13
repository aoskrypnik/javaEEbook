package com.skrypnik.javaee.service.impl;

import com.skrypnik.javaee.exception.ModelAlreadyExistsException;
import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.repository.BookRepository;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultBookService implements BookService {

	private static final String BOOK_ALREADY_EXISTS_MESSAGE = "Book %s already exists";
	private static final char PERCENT = '%';

	private final BookRepository bookRepository;

	@Override
	public Book save(Book book) {
		if (nonNull(bookRepository.findByIsbn(book.getIsbn()))) {
			throw new ModelAlreadyExistsException(String.format(BOOK_ALREADY_EXISTS_MESSAGE, book.getIsbn()));
		}
		return bookRepository.save(book);
	}

	@Override
	public Book getByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Override
	public Page<Book> get(String searchString, Pageable pageable) {
		Specification<Book> bookSpecification = (root, criteriaQuery, criteriaBuilder) ->
				criteriaBuilder.or(
						criteriaBuilder.like(root.get("isbn"), PERCENT + searchString + PERCENT),
						criteriaBuilder.like(root.get("title"), PERCENT + searchString + PERCENT),
						criteriaBuilder.like(root.get("author"), PERCENT + searchString + PERCENT)
				);
		return bookRepository.findAll(bookSpecification, pageable);
	}

	@PostConstruct
	private void initDataBase() {
		bookRepository.save(new Book("1234567899999", "Chorna rada", "Panteleymon Kulish"));
		bookRepository.save(new Book("1234567888888", "Zapovit", "Taras Shevchenko"));
		bookRepository.save(new Book("1234567777777", "Haydamaky", "Taras Shevchenko"));
		bookRepository.save(new Book("1234567666666", "Hiba revut voly", "Panas Myrniy"));
		bookRepository.save(new Book("1234565555555", "Losiva pisnya", "Lesia Ukrainka"));
	}
}
