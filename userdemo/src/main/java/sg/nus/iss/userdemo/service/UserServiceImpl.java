package sg.nus.iss.userdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.nus.iss.userdemo.Repo.UserRepository;
import sg.nus.iss.userdemo.model.User;
@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository uRepo;
	
	@Autowired
	public UserServiceImpl(UserRepository uRepo) {
		this.uRepo = uRepo;
	}

	@Override
	public List<User> getAllUsers() {
		
		return uRepo.findAll();
	}
	
	@Override
	public User findUserById(Long id) {
		
		return uRepo.findById(id).get();
	}

	@Override
	public void addNewUser(User user) {
		// we assume email is unique for a user
		User userByEmail = uRepo.findUserByEmail(user.getEmail());
		
		if (userByEmail != null) {
			throw new IllegalStateException("email taken");
		}
		uRepo.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		
		boolean exists = uRepo.existsById(userId);
		
		if (!exists) {
			throw new IllegalStateException("user with id" + userId + "doesn't exist");
		}
		uRepo.deleteById(userId);
	}

	@Override
	public void updateUser(Long userId, String name, String email) {
		
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new IllegalStateException(
						"user with id" + userId + "doesn't exist"));
		
		if (name != null 
				&& name.length() > 0 
				&& !Objects.equals(user.getName(), name)) {
					user.setName(name);
						}		
		
		if (email != null 
				&& email.length() > 0 
				&& !Objects.equals(user.getEmail(), email)) {
					User userByEmail = 
					uRepo.findUserByEmail(user.getEmail());
			if (userByEmail != null) {
				throw new IllegalStateException ("email taken");
			}
			
			user.setEmail(email);
		}
	}

	@Override
	public Set<User> getAllFollowersbyuserId(Long userId) {
		
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new IllegalStateException(
						"user with id" + userId + "doesn't exist"));
		Set<User> followers = user.getFollowers();
		
		return followers;

	}

	@Override
	public Set<User> getAllFollowingsbyuserId(Long userId) {
		User user = uRepo.findById(userId)
				.orElseThrow(() -> new IllegalStateException(
						"user with id" + userId + "doesn't exist"));
		Set<User> followings = user.getFollowings();
		
		return followings;
	}


	@Override
	public List<User> findUsersByName(String username) {
		
		return uRepo.findUsersByName(username);
	}

	@Override
	public List<User> getNearbyFollowers(double x, double y) {
		
		return uRepo.getFollowersByDistanceFromUser(x, y);
	}
	
	

	

}

	

