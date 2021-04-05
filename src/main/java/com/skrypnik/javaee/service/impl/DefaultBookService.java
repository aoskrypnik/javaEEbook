package com.skrypnik.javaee.service.impl;

import com.skrypnik.javaee.dto.BookDto;
import com.skrypnik.javaee.exception.ModelAlreadyExistsException;
import com.skrypnik.javaee.exception.ModelNotFoundException;
import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.model.UserEntity;
import com.skrypnik.javaee.repository.BookRepository;
import com.skrypnik.javaee.repository.UserRepository;
import com.skrypnik.javaee.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultBookService implements BookService {

	private static final String BOOK_ALREADY_EXISTS_MESSAGE = "Book %s already exists";
	private static final String USER_NOT_FOUND_MESSAGE = "User %s not found";
	private static final char PERCENT = '%';

	private final BookRepository bookRepository;
	private final UserRepository userRepository;

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
	public Page<BookDto> search(String searchString, Pageable pageable, String username) {
		Specification<Book> bookSpecification = (root, criteriaQuery, criteriaBuilder) ->
				criteriaBuilder.or(
						criteriaBuilder.like(root.get("isbn"), PERCENT + searchString + PERCENT),
						criteriaBuilder.like(root.get("title"), PERCENT + searchString + PERCENT),
						criteriaBuilder.like(root.get("author"), PERCENT + searchString + PERCENT)
				);
		List<String> favouriteBooksIsbns = getUserEntityByUsername(username).getFavouriteBooks().stream()
				.map(Book::getIsbn)
				.collect(toList());
		return bookRepository.findAll(bookSpecification, pageable)
				.map(book -> new BookDto(book, favouriteBooksIsbns.contains(book.getIsbn())));
	}

	@Override
	public List<BookDto> getFavouriteByUsername(String username) {
		return getUserEntityByUsername(username).getFavouriteBooks()
				.stream().map(book -> new BookDto(book, true))
				.collect(toList());
	}

	@Override
	public void addToFavourite(String isbn, String username) {
		UserEntity userEntity = getUserEntityByUsername(username);
		userEntity.getFavouriteBooks().add(bookRepository.getOne(isbn));
	}

	@Override
	public void removeFromFavourite(String isbn, String username) {
		UserEntity userEntity = getUserEntityByUsername(username);
		userEntity.getFavouriteBooks().removeIf(book -> book.getIsbn().equals(isbn));
	}

	private UserEntity getUserEntityByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ModelNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
	}

	@PostConstruct
	private void initDataBase() {
		bookRepository.save(new Book("978-1-891830-85-3", "Chorna rada", "Panteleymon Kulish"));
		bookRepository.save(new Book("978-1-60309-265-4", "Zapovit", "Taras Shevchenko"));
		bookRepository.save(new Book("978-1-60309-077-3", "Haydamaky", "Taras Shevchenko"));
		bookRepository.save(new Book("978-1-60309-369-9", "Hiba revut voly", "Panas Myrniy"));
		bookRepository.save(new Book("978-1-60309-026-1", "Losiva pisnya", "Lesia Ukrainka"));
	}
}
