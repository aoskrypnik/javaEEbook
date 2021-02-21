package com.skrypnik.javaee.controller;

import com.skrypnik.javaee.model.Book;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerIntegrationTest {

	private Book book = new Book("isbn", "title", "author");

	@LocalServerPort
	void savePort(int port) {
		RestAssured.port = port;
	}

	@Test
	void shouldSaveAndReturnBook() {
		RestAssured
				.given()
				.body(book)
				.contentType(ContentType.JSON)
				.when()
				.post("/api/books")
				.then()
				.statusCode(200)
				.body("isbn", CoreMatchers.is("isbn"))
				.body("title", CoreMatchers.is("title"))
				.body("author", CoreMatchers.is("author"));
	}

	@Test
	void shouldReturnAllBooks() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.when()
				.get("/api/books")
				.then()
				.statusCode(200)
				.body("", hasItems(hasEntry("author", "Taras Shevchenko"), hasEntry("isbn", "1234567888888"), hasEntry("title", "Zapovit")),
						"", hasItems(hasEntry("author", "Panteleymon Kulish"), hasEntry("isbn", "1234567899999"), hasEntry("title", "Chorna rada")));
	}

	@Test
	void shouldReturnOneBook() {
		RestAssured
				.given()
				.queryParam("searchString", "Zapovit")
				.contentType(ContentType.JSON)
				.when()
				.get("/api/books")
				.then()
				.statusCode(200)
				.body("", hasItems(hasEntry("author", "Taras Shevchenko"), hasEntry("isbn", "1234567888888"), hasEntry("title", "Zapovit")),
						"size()", is(1));
	}
}
