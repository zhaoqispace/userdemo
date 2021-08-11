package sg.nus.iss.userdemo.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.nus.iss.userdemo.model.User;
import sg.nus.iss.userdemo.service.UserService;


import sg.nus.iss.userdemo.request.UserFriendsRequestEntity;


@RestController
public class UserDemoApiController {
	
	private final Logger logger = 
			LoggerFactory.getLogger(UserDemoApiController.class);
	
	private UserService uService;
	

	@Autowired
	public UserDemoApiController(UserService uService) {
		this.uService = uService;
	}
	
	
	// test OAuth2 login, no need to login
	@GetMapping("/")
	public String helloWorld() {
		return "Hello World! you don't need to be logged in.";
	}
	
	// test OAuth2 login, need to login
	@GetMapping("/restricted")
	public String restricted() {
		return "if you see this you are logged in";
	}
	
	
	// get all users
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> userlist= uService.findAllUsers();
			if (userlist.isEmpty() || 
					userlist.size() == 0) {
				return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<User>>(userlist, HttpStatus.OK);
		}catch(NoSuchElementException ex) {
			// log exception first, then return Conflict
	        logger.error(ex.getMessage());
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}	
	}
	
	// get a user by id
	@GetMapping("/users/{userid}")
	public ResponseEntity<User> getUserbyId(@PathVariable("userid") int id) {
			
		try {
			User user = uService.findUserById(id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}catch(NoSuchElementException ex) {
			// log exception first, then return Conflict
	        logger.error(ex.getMessage());
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}
	
	// add a new user
	@PostMapping("/users/adduser")
	public ResponseEntity<User> addNewUser(@RequestBody User user) {
		try {
			User newuser = uService.addNewUser(user);
			return new ResponseEntity<User>(newuser, HttpStatus.CREATED);
			
		} catch (Exception ex) {
			// log exception first, then return Conflict
			logger.error(ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// update a user
	@PutMapping("/users/update/{userid}")
	public ResponseEntity<User> updateUser (
				@PathVariable("userid") int userId, 
				@Valid @RequestBody User userDetails) {
	
		try {
			return new ResponseEntity<User>(
					uService.updateUser(userId, userDetails), HttpStatus.OK);
			
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// delete a user by id
	@DeleteMapping("/users/delete/{userid}") 
	public ResponseEntity<HttpStatus> deleteUser(
			@PathVariable("userid") int userId) {	
		try {
				uService.deleteUser(userId);
				return new ResponseEntity<HttpStatus>(
						HttpStatus.NO_CONTENT);
			
		}catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<HttpStatus>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
		
	// add friend by providing two emails
	// whenever there is a userFreindRequest, 
	// both users will be friends to each other,
	// which means we will add user2 to the friendlist of user1
	// and add user1 to the friendlist of user2
	@PostMapping("/users/userfriendsrequest")
	public ResponseEntity<Map<String, Object>> userFriendRequest(
			@RequestBody UserFriendsRequestEntity userFriendsRequestEntity) {
		
		return uService.addUserFriends(userFriendsRequestEntity);
	}
	
	
	// get all friends for a user
	@GetMapping("/users/friends/{userid}")
	public ResponseEntity<Set<String>> getAllFriendsByuserId(
			@PathVariable("userid") int userId) {

		try {
			//show the email instead of user object
			Set<String> allFriendsByUserId= 
					uService.getAllFriendsByUserId(userId);		
			
			return new ResponseEntity<Set<String>>(
					allFriendsByUserId, HttpStatus.OK);
		} catch(NoSuchElementException ex) {
			// log exception first, then return Conflict
	        logger.error(ex.getMessage());
			return new ResponseEntity<Set<String>>(
					HttpStatus.NOT_FOUND);
		}	
	}
	
	
	// get friends nearby
	@GetMapping("/users/friendsnearby/{name}")
	public ResponseEntity<List<String>> getFriendsNearby(
			@PathVariable("name") String name) {
		
		try {
			// only show the top 2 nearest friends
			List<String> friendsNearby = 
					uService.getFriendsByDistance(name);
			
			return new ResponseEntity<List<String>>(
					friendsNearby, HttpStatus.OK);
			
		} catch(NoSuchElementException ex) {
			// log exception first, then return Conflict
	        logger.error(ex.getMessage());
	        
			return new ResponseEntity<List<String>>(
					HttpStatus.NOT_FOUND);
		}	
	}

}
