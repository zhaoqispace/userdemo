package sg.nus.iss.userdemo.model;

import java.awt.Point;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.springframework.util.CollectionUtils;


@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@Past
	private LocalDate dob;
	
	private Point address;
	
	private String description;
	@NotEmpty
	private String email;
	private LocalDate createAt;
	
	
	@ManyToMany
	@JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "userId") , inverseJoinColumns = @JoinColumn(name = "friendId") )
	private Set<User> userFriends;
	

	// add userFriends
	public void addUserFriends(User user) {
		if (CollectionUtils.isEmpty(this.userFriends)) {
			this.userFriends = new HashSet<>();
		}
		this.userFriends.add(user);
	}
	

	// constructor without arguments
	public User() { }
	
	
	// constructors for test purpose	
	
	public User(int id, String name, @Past LocalDate dob, String description, @NotEmpty String email) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.description = description;
		this.email = email;
	}
	
	public User( String name, @Past LocalDate dob, String description, @NotEmpty String email) {
		super();
		this.name = name;
		this.dob = dob;
		this.description = description;
		this.email = email;
	}

	
	public User(int id, String name, @Past LocalDate dob, String description, @NotEmpty String email,
			LocalDate createAt) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.description = description;
		this.email = email;
		this.createAt = createAt;
	}
	
	public User(int id, String name, @Past LocalDate dob, Point address, String description, @NotEmpty String email,
			LocalDate createAt) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.address = address;
		this.description = description;
		this.email = email;
		this.createAt = createAt;
	}
	
	
	
	
	// constructors including all attributes
	public User(int id, String name, @Past LocalDate dob, Point address, String description, @NotEmpty String email,
			LocalDate createAt, Set<User> followers, Set<User> userFriends) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.address = address;
		this.description = description;
		this.email = email;
		this.createAt = createAt;
		this.userFriends = userFriends;
	}
	


	// Getters and Setters
	


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Point getAddress() {
		return address;
	}

	public void setAddress(Point address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getCreateAt() {
		return createAt;
	}

	public void setCreateAt(LocalDate createAt) {
		this.createAt = createAt;
	}


	public Set<User> getUserFriends() {
		return userFriends;
	}

	public void setUserFriends(Set<User> userFriends) {
		this.userFriends = userFriends;
	}
	
	

}
