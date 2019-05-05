package vit.metro.repo;

import org.springframework.data.repository.CrudRepository;

import vit.metro.entity.Authority;

public interface AuthorityRepository 
	extends CrudRepository<Authority, String> {
	
	Authority findByAuthority(String authority);
}
