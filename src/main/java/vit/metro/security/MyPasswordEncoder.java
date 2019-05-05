package vit.metro.security;

import org.springframework.security.crypto.password.PasswordEncoder;

//used in dev for testing
public class MyPasswordEncoder implements PasswordEncoder {
	
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
