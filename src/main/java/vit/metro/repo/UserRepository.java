package vit.metro.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import vit.metro.entity.User;

public interface UserRepository 
	extends CrudRepository<User, String> {
	
	User findByUsername(String username);
	List<User> findByUsernameNot(String username);
}
