package com.skrypnik.javaee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skrypnik.javaee.dto.BookDto;
import com.skrypnik.javaee.model.Book;
import com.skrypnik.javaee.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(BookRestController.class)
@WithMockUser(username = "username")
class BookRestControllerTest {

	private static final String EMPTY_SEARCH_STRING = "";
	private static final String USERNAME = "username";
	private static final String SEARCH_STRING = "is";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BookService bookService;

	private Book book1;
	private Book book2;
	private BookDto bookDto1;
	private BookDto bookDto2;

	@BeforeEach
	void setUp() {
		book1 = new Book("isbn", "title1", "author1");
		book2 = new Book("nbsi", "title2", "author2");
		bookDto1 = new BookDto(book1, false);
		bookDto2 = new BookDto(book2, false);
		Authentication authentication = Mockito.mock(Authentication.class);
		when(authentication.getName()).thenReturn(USERNAME);
		PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "title"));
		when(bookService.search(EMPTY_SEARCH_STRING, pageRequest, USERNAME)).thenReturn(new PageImpl<>(List.of(bookDto1, bookDto2)));
		when(bookService.search(SEARCH_STRING, pageRequest, USERNAME)).thenReturn(new PageImpl<>(List.of(bookDto1)));
		when(bookService.save(book1)).thenReturn(book1);
	}

	@Test
	void shouldReturnTwoBooksWhenSearchStringIsEmpty() throws Exception {
		String twoBooksJson = objectMapper.writeValueAsString(List.of(bookDto1, bookDto2));
		ResultActions resultActions = mockMvc.perform(
				get("/api/books")
		)
				.andExpect(MockMvcResultMatchers.status().isOk());
		String actualResponseString = resultActions.andReturn().getResponse().getContentAsString();
		assertThat(actualResponseString).contains(twoBooksJson);
	}

	@Test
	void shouldReturnOneBooksWhenSearchStringMatchesOnlyOneBook() throws Exception {
		String oneBookJson = objectMapper.writeValueAsString(List.of(bookDto1));
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