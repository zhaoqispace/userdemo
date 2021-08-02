package sg.nus.iss.userdemo.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import sg.nus.iss.userdemo.model.User;


public interface UserService {

	
	
	List<User> getAllUsers();
	
	User findUserById(Long id);

	void addNewUser(User user);

	void deleteUser(Long userId);

	void updateUser(Long userId, String name, String email);

	Set<User> getAllFollowersbyuserId(Long userId);

	Set<User> getAllFollowingsbyuserId(Long userId);

	List<User> findUsersByName(String username);

	List<User> getNearbyFollowers(double x, double y);



}
