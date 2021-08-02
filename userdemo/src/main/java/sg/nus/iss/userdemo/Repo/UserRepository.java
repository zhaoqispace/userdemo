package sg.nus.iss.userdemo.Repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.nus.iss.userdemo.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

		User findUserByEmail(String email);

		
		@Query(value = "SELECT u.followers,ST_Distance(u.address,ST_SetSRID(ST_Point(:x,:y),4326)) AS distance "
	            + "FROM Useru "
	            + "ORDER BY u.address <-> ST_SetSRID(ST_Point(:x,:y),4326) "
	            + "LIMIT 5"
	             , nativeQuery = true)
		List<User> getFollowersByDistanceFromUser(@Param("x") Double x, @Param("y") Double y);

		List<User> findUsersByName(String username);
		
	
}
