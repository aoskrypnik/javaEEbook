package com.skrypnik.javaee.repository;

import com.skrypnik.javaee.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

	Book findByIsbn(String isbn);

	@Query(value = "SELECT b FROM Book b " +
			"WHERE b.isbn LIKE CONCAT('%',:searchString,'%') " +
			"OR b.author LIKE CONCAT('%',:searchString,'%')" +
			"OR b.title LIKE CONCAT('%',:searchString,'%')")
	List<Book> findByIsbnOrTitleOrAuthorContains(String searchString);
}
