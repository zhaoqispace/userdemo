package sg.nus.iss.userdemo;

import java.awt.Point;
import java.net.http.HttpHeaders;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.nus.iss.userdemo.controller.UserDemoApiController;
import sg.nus.iss.userdemo.model.User;
import sg.nus.iss.userdemo.service.UserService;


@SpringBootTest
@AutoConfigureMockMvc
public class UserDemoApiControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;
	
	@MockBean
	UserService uService;
	
	@Autowired
    WebApplicationContext wac;
	
	
	// define dummy data
	User user1 = new User(1, "Alex", LocalDate.of(1990, 02, 18), 
			"Hello I am Alex", "alex@gmail.com", LocalDate.now());
	
	User user2 = new User(2, "Max", LocalDate.of(1987, 03, 05), 
			"Hello I am Max", "max@gmail.com", LocalDate.now());
	
	User user3 = new User(3, "Lily", LocalDate.of(1988, 11, 20), 
			"Hello I am Lily, I am the Admin", "lily@gmail.com", LocalDate.now());
	
	User user4 = new User(4, "CY", LocalDate.of(1993, 9, 6), 
			"Hello I am CY", "cy@gmail.com", LocalDate.now());
	
	User user5 = new User(5, "Brandon", LocalDate.of(1992, 5, 1), 
			"Hello I am Brandon", "brandon@gmail.com", LocalDate.now());
	
	
	
	// test the method getAllUsers()
	@Test
	public void getAllUsersTest() throws Exception{
		List<User> records = new ArrayList<User>();
		records.add(user1);
		records.add(user2);
		records.add(user3);
		records.add(user4);
		records.add(user5);
		
		Mockito.when(uService.getAllUsers()).thenReturn(records);
		
		// Execute the GET request
		mockMvc.perform(get("/users"))
				// validate the response code and content type
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// validate the returned fields
				.andExpect(jsonPath("$", hasSize(5)))
				.andExpect(jsonPath("$[0].name", is("Alex")))
				.andExpect(jsonPath("$[1].name", is("Max")))
				.andExpect(jsonPath("$[2].name", is("Lily")))
				.andExpect(jsonPath("$[3].name", is("CY")))
				.andExpect(jsonPath("$[4].name", is("Brandon")));
				
	}
	
	// Test the method getUserById()
	@Test
	public void getUserbyIdTest() throws Exception{
		
		Mockito.when(uService.findUserById(1)).thenReturn(user1);
		
			mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.name", is("Alex")));
	}
	
	// Test the method addNewUser()
	@Test
	public void addNewUserTest() throws Exception {
		User user6 = new User(6, "Ronnie", LocalDate.of(1987, 6, 1), 
				"Hello I am Ronnie", "ronnie@gmail.com", LocalDate.now());
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("users/adduser")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(user6));

			mockMvc.perform(mockRequest)
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.name", is("Ronnie")))
					.andExpect(jsonPath("$.description", is("Hello I am Ronnie")))
					.andExpect(jsonPath("$.email", is("ronnie@gmail.com")));

	}			
	
	// Test the method deleteUser()
	@Test
	public void delelteUserTest() throws Exception{
		int userid = 1;
		Mockito.when(uService.findUserById(userid)).thenReturn(user1);

		mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
	}
	
	// Test the method updateUser()
	@Test
	public void updateUserTest() throws Exception {

		// update name and email
		user2.setName("max123");
		user2.setEmail("max123@gmail.com");
		
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/users/update/2")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(user2));
		
		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.name", is("max123")))
				.andExpect(jsonPath("$.email",is("max123@gmail")));
	
	}
}
