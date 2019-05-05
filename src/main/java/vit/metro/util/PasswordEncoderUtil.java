package vit.metro.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
	public static void main(String[] args ) {
		String plainPassword = "123";
		String encodedPassword = new BCryptPasswordEncoder().encode(plainPassword);
		System.out.println(encodedPassword);
	} 	
}
