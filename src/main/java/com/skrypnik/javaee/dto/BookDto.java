package com.skrypnik.javaee.dto;

import com.skrypnik.javaee.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {
	private String isbn;
	private String title;
	private String author;
	private boolean liked;

	public BookDto(Book book, boolean liked) {
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.liked = liked;
	}
}
