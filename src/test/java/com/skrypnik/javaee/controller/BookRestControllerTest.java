package com.skrypnik.javaee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

	private static final String EMPTY_SEARCH_STRING = "";
	private static final String SEARCH_STRING = "is";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BookService bookService;

	private Book book1;
	private Book book2;

	@BeforeEach
	void setUp() {
		book1 = new Book("isbn", "title1", "author1");
		book2 = new Book("nbsi", "title2", "author2");

		PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "title"));
		when(bookService.get(EMPTY_SEARCH_STRING, pageRequest)).thenReturn(new PageImpl<>(List.of(book1, book2)));
		when(bookService.get(SEARCH_STRING, pageRequest)).thenReturn(new PageImpl<>(List.of(book1)));
		when(bookService.save(book1)).thenReturn(book1);
	}

	@Test
	void shouldReturnTwoBooksWhenSearchStringIsEmpty() throws Exception {
		String twoBooksJson = objectMapper.writeValueAsString(List.of(book1, book2));
		ResultActions resultActions = mockMvc.perform(
				get("/api/books")
		)
				.andExpect(MockMvcResultMatchers.status().isOk());
		String actualResponseString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(actualResponseString).contains(twoBooksJson);
	}

	@Test
	void shouldReturnOneBooksWhenSearchStringMatchesOnlyOneBook() throws Exception {
		String oneBookJson = objectMapper.writeValueAsString(List.of(book1));
		ResultActions resultActions = mockMvc.perform(
				get("/api/books")
						.param("searchString", SEARCH_STRING)
		)
				.andExpect(MockMvcResultMatchers.status().isOk());

		String actualResponseString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(actualResponseString).contains(oneBookJson);
	}

	@Test
	void shouldReturnSavedBookWhenPostRequestIsSuccessful() throws Exception {
		String oneBookJson = objectMapper.writeValueAsString(book1);
		mockMvc.perform(
				post("/api/books")
						.content(oneBookJson)
						.contentType(MediaType.APPLICATION_JSON)
		)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(oneBookJson));
	}
}