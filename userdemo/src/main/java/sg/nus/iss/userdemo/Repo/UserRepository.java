package sg.nus.iss.userdemo.Repo;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.nus.iss.userdemo.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

		
		User findByEmail(String email);
		List<User> findUsersByName(String username);
}
