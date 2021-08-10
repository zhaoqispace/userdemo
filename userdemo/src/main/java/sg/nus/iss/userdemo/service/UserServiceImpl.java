package sg.nus.iss.userdemo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.Point;
import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import sg.nus.iss.userdemo.Repo.UserRepository;
import sg.nus.iss.userdemo.model.User;
import sg.nus.iss.userdemo.request.UserFriendsRequestEntity;


@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository uRepo;
	
	@Autowired
	public UserServiceImpl(UserRepository uRepo) {
		this.uRepo = uRepo;
	}

	// get all users
	@Override
	public List<User> getAllUsers() {
		
		return uRepo.findAll();
	}
	
	// find all users by name
	@Override
	public List<User> findUsersByName(String username) {
		
		return uRepo.findUsersByName(username);
	}
	
	// find user by Id
	@Override
	public User findUserById(int id) {
		
		return uRepo.findById(id).get();
	}

	// add new user
	@Override
	public User addNewUser(User user) {
		// we assume email is unique for a user
		Optional<User> exists = uRepo.findById(user.getId());
		
		if (exists.isPresent()) {
			throw new IllegalStateException("email taken");
		}
		User newUser = uRepo.save(user);	
		return newUser;
	}

	// delete user
	@Override
	public void deleteUser(int userId) {
		
		boolean exists = uRepo.existsById(userId);
		
		if (!exists) {
			throw new IllegalStateException("user with id" + userId + "doesn't exist");
		}
		uRepo.deleteById(userId);
	}
	

	// update a user
	@Override
	public User updateUser(int userId, @Valid User userDetails) {
		User userFound = uRepo.findById(userId)
				.orElseThrow(() -> new IllegalStateException(
						"user with id" + userId + "doesn't exist"));
		userFound.setName(userDetails.getName());
		userFound.setDob(userDetails.getDob());
		userFound.setDescription(userDetails.getDescription());
		userFound.setEmail(userDetails.getEmail());
			
		
		User updatedUser = uRepo.save(userFound);
		return updatedUser;
		
	}
	
	
	
	//given an email, if user not exists, save user; if user exists, return user.
	private User saveIfNotExist(String email) {

		User existingUser = this.uRepo.findByEmail(email);
		if (existingUser == null) {
			existingUser = new User();
			existingUser.setEmail(email);
			return this.uRepo.save(existingUser);
		} else {
			return existingUser;
		}

	}
	
	
	// add friends with two different emails
	@Override
	public ResponseEntity<Map<String, Object>> addUserFriends(UserFriendsRequestEntity userFriendsRequestEntity) {

		Map<String, Object> result = new HashMap<String, Object>();

		if (userFriendsRequestEntity == null) {
			result.put("Error : ", "Invalid request");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}

		if (CollectionUtils.isEmpty(userFriendsRequestEntity.getFriends())) {
			result.put("Error : ", "Friend list cannot be empty");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}
		if (userFriendsRequestEntity.getFriends().size() != 2) {
			result.put("Info : ", "Please provide 2 emails to make them friends");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}

		String email1 = userFriendsRequestEntity.getFriends().get(0);
		String email2 = userFriendsRequestEntity.getFriends().get(1);

		if (email1.equals(email2)) {
			result.put("Info : ", "Cannot make friends, if users are same");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}

		User user1 = null;
		User user2 = null;
		user1 = this.saveIfNotExist(email1);
		user2 = this.saveIfNotExist(email2);

		if (user1.getUserFriends().contains(user2)) {
			result.put("Info : ", "Can't add, they are already friends");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		}

		// add user2 to the friend list of user1
		user1.addUserFriends(user2);
		this.uRepo.save(user1);
		
		// add user1 to the friend list of user2
		user2.addUserFriends(user1);
		this.uRepo.save(user2);
		
		result.put("Success", true);

		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
	// get all friends by user id
	@Override
	public Set<String> getAllFriendsByUserId(int userId) {
			
		User user = uRepo.findById(userId)
					.orElseThrow(() -> new IllegalStateException(
							"user with id" + userId + "doesn't exist"));
			
		// friends can be null
		Set<String> friendsEmails = user.getUserFriends().stream().map(User::getEmail).collect(Collectors.toSet());		
		
		return friendsEmails;
	}
	

	// get nearby friends
	@Override
	public List<String> getFriendsByDistance(String name) {
		
		Set<Double> distanceFromFriends = new HashSet<Double>();
		Map<User, Double> friends_distances = new HashMap<User, Double>();
		Map<User, Double> friends_distances_sorted = new HashMap<User, Double>();
		List<String> FriendsEmail_distances_sorted = new ArrayList<String>();
		
		// there may be more than one user having the same username
		List<User> usersByName = uRepo.findUsersByName(name);
		
		if (CollectionUtils.isEmpty(usersByName)) {
			throw new IllegalStateException("user with name" + name + "doesn't exist");
		}
		
		for(User user: usersByName) {
			
			// get the user's address coordinates
			double userx = user.getAddress().getX();
			double usery = user.getAddress().getY();
			
			// get the friends of the user
			Set<User> friends = user.getUserFriends();
			
			for (User friend: friends) {
				double friendx = friend.getAddress().getX();
				double friendy = friend.getAddress().getY();
				double distance = Math.sqrt((userx - friendx) * (userx - friendx) + (usery - friendy) *  (usery - friendy));	
				// put in the map: friend is key, distance is value
				friends_distances.put(friend, distance);
			}
		}
		
		// sort the map
		friends_distances.entrySet().stream()
        .sorted(Map.Entry.<User, Double>comparingByValue())
        .forEachOrdered(x -> friends_distances_sorted.put(x.getKey(), x.getValue()));
		
		
		for(User friend: friends_distances_sorted.keySet()) {
			// get the top two nearest friends
			if (FriendsEmail_distances_sorted.size() < 2) {
				FriendsEmail_distances_sorted.add(friend.getEmail());
			}	
		}
		
		return FriendsEmail_distances_sorted;
	}

}

	

