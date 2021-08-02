package sg.nus.iss.userdemo.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sg.nus.iss.userdemo.model.User;
import sg.nus.iss.userdemo.service.UserService;


@RestController
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private UserService uService;
	

	@Autowired
	public UserController(UserService uService) {
		this.uService = uService;
	}
	
	@GetMapping("/")
	public String helloWorld() {
		return "Hello World! you don't need to be logged in.";
	}
	
	@GetMapping("/restricted")
	public String restricted() {
		return "if you see this you are logged in";
	}
	
	// get all users
	@GetMapping("/users")
	public List<User> getAllUsers() {
		return uService.getAllUsers();
	}
	
	// get a user by id
	@GetMapping("/users/{userid}")
	public ResponseEntity<User> getUserbyId(@PathVariable Long id) {
		try {
			User user = uService.findUserById(id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}
	
	// add a new user
	@PostMapping("/users/adduser")
	public void addNewUser(@RequestBody User user) {
		uService.addNewUser(user);
	}
	
	// delete a user by id
	@DeleteMapping("/users/delete/{userid}") 
	public void deleteUser(@PathVariable("userid") Long userId) {
		uService.deleteUser(userId);
	}
	
	// update a user's name and email
	@PutMapping(path = "{/users/update/{userid}")
	public void updateUser (
			@PathVariable("userId") Long userId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email) {
		uService.updateUser(userId, name, email);
	}
	
	// retrieve all the followers
	@GetMapping("/users/{userid}/followers")
	public Set<User> getAllFollowers(@PathVariable("userid") Long userId) {
		return uService.getAllFollowersbyuserId(userId);
	}
	
	// retrieve all the followings
	@GetMapping("/users/{userid}/followings")
	public Set<User> getAllFollowings(@PathVariable("userid") Long userId) {
		return uService.getAllFollowingsbyuserId(userId);
	}
	
	
	
	// find nearby followers
	@GetMapping("/users/{username}/followers")
	public List<User> getNearbyFollowers(@PathVariable("username") String username) {
		
		List<User> followersNearby = new ArrayList<User>();
		
		List<User> usersByName = uService.findUsersByName(username);
		
		for (User user: usersByName) {
			Point address = user.getAddress();
			double x = address.getX();
			double y = address.getY();
			followersNearby = uService.getNearbyFollowers(x, y);
		}

		return followersNearby;
	}
	
	

}
