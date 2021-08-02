package sg.nus.iss.userdemo.model;

import java.awt.Point;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

@Entity
@Table
public class User {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Past
	private LocalDate dob;
	
//	private String address;
	
	private Point address;
	
	private String description;
	@NotEmpty
	private String email;
	private LocalDate createAt;
	
	private RoleType role;
	
	@ManyToMany
	@JoinTable(name="followerTable")
	private Set<User> followers;
	
	@ManyToMany
	@JoinTable(name="followingTable")
	private Set<User> followings;
	
	// constructor without arguments
	public User() { }
	
	
	// constructor for test purpose
	public User(Long id, String name, @Past LocalDate dob, String description, @NotEmpty String email,
			LocalDate createAt, RoleType role) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.description = description;
		this.email = email;
		this.createAt = createAt;
		this.role = role;
	}
	

	
	// constructors including all attributes
	public User(Long id, String name, @Past LocalDate dob, Point address, String description, @NotEmpty String email,
			LocalDate createAt, RoleType role, Set<User> followers, Set<User> followings) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.address = address;
		this.description = description;
		this.email = email;
		this.createAt = createAt;
		this.role = role;
		this.followers = followers;
		this.followings = followings;
	}
	
	


	// Getters and Setters



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public Set<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public Set<User> getFollowings() {
		return followings;
	}

	public void setFollowings(Set<User> followings) {
		this.followings = followings;
	}
	
	

}
