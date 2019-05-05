package vit.metro.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import vit.metro.entity.Authority;
import vit.metro.entity.User;
import vit.metro.exception.DuplicateUserException;
import vit.metro.exception.UserNotFoundException;
import vit.metro.repo.AuthorityRepository;
import vit.metro.repo.UserRepository;
import vit.metro.util.UserDetailDto;
import vit.metro.util.UserDto;

@RestController
@RequestMapping(path="/users", produces="application/json")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AuthorityRepository authRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//get user by login
	@GetMapping("/{name}")
	@PreAuthorize(value="isAuthenticated()")
	public ResponseEntity<UserDetailDto> getUserByLogin(@PathVariable("name") String login) {
		User user = userRepo.findByUsername(login);
		if(user == null) {
			throw new UserNotFoundException("User " + login + " is not found");
		}
		UserDetailDto dto = new UserDetailDto(user);
		return new ResponseEntity(dto, HttpStatus.FOUND);
	}
	
	//get list of all users except superuser "sup"
	@GetMapping
	@PreAuthorize(value="isAuthenticated()")
	public ResponseEntity<List<UserDto>> getAllUsersExceptRoot() {
		List<User> users = userRepo.findByUsernameNot("sup");
		List<UserDto> list = new ArrayList<>();
		for (User user : users) {
			list.add(new UserDto(user));
		}
		return new ResponseEntity(list, HttpStatus.FOUND);
	}
	
	//create a new user
	@PostMapping(consumes="application/json")
	@PreAuthorize("hasAuthority('create')")
	public ResponseEntity<UserDetailDto> createUser(@RequestBody @Valid User user) {
		String newName = user.getUsername();
		User oldUser = userRepo.findByUsername(newName);
		if(oldUser != null) {
			throw new DuplicateUserException("Login " + newName + " already exists. Please, use another username.");
		}
		User newUser = new User();
		newUser.setUsername(newName);
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		newUser.setEnabled(1);
		List<Authority> newAuthorities = new ArrayList<>();
		List<Authority> authorities = user.getAuthorities();
		for (Authority auth : authorities) {
			String name = auth.getAuthority();
			Authority authority = authRepo.findByAuthority(name);
			newAuthorities.add(authority);
		}
		newUser.setAuthorities(newAuthorities);
		UserDetailDto dto = new UserDetailDto(userRepo.save(newUser));
		return new ResponseEntity(dto, HttpStatus.CREATED);
	}
	
	//update a user
	@PatchMapping(consumes="application/json")
	@PreAuthorize("hasAuthority('update')")
	public ResponseEntity<UserDetailDto> updateUser(@RequestBody @Valid User patch) {
		String login = patch.getUsername();
		User user = userRepo.findByUsername(login);
		if(user == null) {
			throw new UserNotFoundException("User " + login + " is not found");
		}		
		if (patch.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(patch.getPassword()));
		}
		if (patch.getEmail() != null) {
			user.setEmail(patch.getEmail());
		}
		if (patch.getAuthorities() != null) {
			List<Authority> newAuthorities = new ArrayList<>();
			List<Authority> authorities = patch.getAuthorities();
			for (Authority auth : authorities) {
				String name = auth.getAuthority();
				Authority authority = authRepo.findByAuthority(name);
				newAuthorities.add(authority);
			}
			user.setAuthorities(newAuthorities);
		}
		UserDetailDto dto = new UserDetailDto(userRepo.save(user));
		return new ResponseEntity(dto, HttpStatus.ACCEPTED);
	}
	
	//delete a user
	@DeleteMapping("/{name}")
	@PreAuthorize("hasAuthority('delete')")
	public ResponseEntity deleteUser(@PathVariable("name") String login) {
		User user = userRepo.findByUsername(login);
		if(user == null) {
			throw new UserNotFoundException("User " + login + " is not found");
		}	
		userRepo.delete(user);
		return new ResponseEntity(HttpStatus.OK);
	}	
}
