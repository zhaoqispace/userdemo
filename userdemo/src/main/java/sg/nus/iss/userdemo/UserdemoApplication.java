package sg.nus.iss.userdemo;

import java.awt.Point;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.nus.iss.userdemo.Repo.UserRepository;
import sg.nus.iss.userdemo.model.User;

@SpringBootApplication
public class UserdemoApplication {
	
	@Autowired
	UserRepository uRepo;

	public static void main(String[] args) {
		SpringApplication.run(UserdemoApplication.class, args);
	}
	
	@Bean
	CommandLineRunner runner() {
		return args -> {
			
			User user1 = new User(1, "Alex", LocalDate.of(1990, 02, 18), new Point(1, 101),
					"Hello I am Alex", "alex@gmail.com", LocalDate.now());
			
			User user2 = new User(2, "Max", LocalDate.of(1987, 03, 05), new Point(1, 102),
					"Hello I am Max", "max@gmail.com", LocalDate.now());
			
			User user3 = new User(3, "Lily", LocalDate.of(1988, 11, 20),  new Point(1, 103),
					"Hello I am Lily", "lily@gmail.com", LocalDate.now());
			
			User user4 = new User(4, "CY", LocalDate.of(1993, 9, 6), new Point(1, 104),
					"Hello I am CY", "cy@gmail.com", LocalDate.now());
			
			User user5 = new User(5, "Brandon", LocalDate.of(1992, 5, 1), new Point(1, 105),
					"Hello I am Brandon", "brandon@gmail.com", LocalDate.now());
			
			uRepo.save(user1);
			uRepo.save(user2);
			uRepo.save(user3);
			uRepo.save(user4);
			uRepo.save(user5);
			
			
		};
	}

}
