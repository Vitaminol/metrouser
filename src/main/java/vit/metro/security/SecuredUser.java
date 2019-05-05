package vit.metro.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vit.metro.entity.Authority;
import vit.metro.entity.User;

public class SecuredUser implements UserDetails {
	private int id;
	private User user;
	
	public SecuredUser(User user){
		this.user = user;
	}
	
	public void setId(int id) {
		this.user.getId();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		List<Authority> aulist = this.user.getAuthorities();
		if(aulist != null){
			for (Authority au : aulist) {
				SimpleGrantedAuthority gauthority = new SimpleGrantedAuthority(au.getAuthority());
				authorities.add(gauthority);
			}
		}
		return authorities;
	}
	
	@Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.getEnabled() == 1 ? true : false;
	}

	public User getUserDetails() {
        return user;
    }
}
