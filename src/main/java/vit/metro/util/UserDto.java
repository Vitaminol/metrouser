package vit.metro.util;

import java.util.ArrayList;
import java.util.List;

import vit.metro.entity.Authority;
import vit.metro.entity.User;

public class UserDto {
	String login;
	List<String> privileges = new ArrayList<>();
	
	public UserDto(User user) {
		this.login = user.getUsername();
		List<Authority> auth = user.getAuthorities();
		if (auth != null) {
			for (Authority a : auth) {
				String name = a.getAuthority();
				privileges.add(name);
			}
		}
		
	}

	public String getLogin() {
		return login;
	}

	public List<String> getPrivileges() {
		return privileges;
	}
	
}
