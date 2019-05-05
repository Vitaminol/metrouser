package vit.metro.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vit.metro.entity.User;
import vit.metro.repo.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		return new SecuredUser(user) ;
	}

}
