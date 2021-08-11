package sg.nus.iss.userdemo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import sg.nus.iss.userdemo.Repo.UserRepository;
import sg.nus.iss.userdemo.model.User;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
	
	
	@Autowired
	private UserRepository urepo;
	
	@Test
	@Order(1)
	void testCreateUser() {
		//given
		User user = new User(6, "Ronnie", LocalDate.of(1987, 6, 1), 
				"Hello I am Ronnie", "ronnie@gmail.com", LocalDate.now());
		
		// when
		User saved = urepo.save(user);
		// then
		assertNotNull(saved);
	}
	
	
	@Test
	@Order(2)
	void testGetUserByEmail() {
		
		//given
		String email= "ronnie@gmail.com";
		
		//when
		User saved = urepo.findByEmail(email);
		
		//then
		assertNotNull(saved);
	}
	
	@Test
	@Order(3)
	public void testUpdateUser() {
		
		//given		
		String email= "ronnie@gmail.com";
		User given = urepo.findByEmail(email);
		
		//when
		given.setEmail("ronnie123@gmail.com");
		User saved = urepo.save(given);
		
		//then
		assertNotNull(saved);

	}	
	
	
	@Test
	@Order(4)
	public void testListUsers() {
		// given
		List<User> list = new ArrayList<User>();
		// when
		list = urepo.findAll();
		// then
		assertTrue(list.size() > 0);
	}
	
	@Test
	@Order(5)
	public void testDeleteUsers() {
		// given
		String email = "ronnie123@gmail.com";
		// when
		User selected = urepo.findByEmail(email);
		urepo.delete(selected);
		// then
		assertTrue(urepo.findByEmail(email) == null);
	}

}
