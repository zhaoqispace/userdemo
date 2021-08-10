package sg.nus.iss.userdemo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import sg.nus.iss.userdemo.model.User;
import sg.nus.iss.userdemo.request.UserFriendsRequestEntity;

public interface UserService {

	
	List<User> getAllUsers();
	
	User findUserById(int id);
	
	User addNewUser(User user);

	void deleteUser(int userId);

	User updateUser(int userId, @Valid User userDetails);

	Set<String> getAllFriendsByUserId(int userId);
	
	
	ResponseEntity<Map<String, Object>> addUserFriends(UserFriendsRequestEntity userFriendsRequestEntity);


	List<User> findUsersByName(String name);

	List<String> getFriendsByDistance(String name);


}
