package vit.metro.util;

import java.util.ArrayList;
import java.util.List;

import vit.metro.entity.Authority;
import vit.metro.entity.User;

public class UserDetailDto {
	
	String login;
	String email;
	int enabled;
	List<String> privileges = new ArrayList<>();
	
	public UserDetailDto(User user) {
		this.login = user.getUsername();
		this.email = user.getEmail();
		this.enabled = user.getEnabled();
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

	public String getEmail() {
		return email;
	}

	public int getEnabled() {
		return enabled;
	}

	public List<String> getPrivileges() {
		return privileges;
	}
	
}
