package vit.metro.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import vit.metro.security.MyPasswordEncoder;
import vit.metro.security.RestAuthenticationEntryPoint;
import vit.metro.security.RestSuccessAuthenticationHandler;
import vit.metro.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
//	private static final String SUCCESS_LOGIN_URL = "/success";
	
//	used for jdbc authentification
//	public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
//			"select username, authority from users join userauthority ua " +
//			"on users.id = ua.userid join authorities a" +
//			"on ua.authid = a.id" +
//			"where username = ?";

//	@Autowired
//	DataSource dataSource;
	
	@Autowired
	RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
	
	@Bean
	public RestSuccessAuthenticationHandler successHandler() {
		return new RestSuccessAuthenticationHandler();
	}
	
	//used for login via browser
//	@Bean 
//	SimpleUrlAuthenticationSuccessHandler successHandler() {
//		SimpleUrlAuthenticationSuccessHandler success = 
//				new SimpleUrlAuthenticationSuccessHandler();
//		success.setDefaultTargetUrl(SUCCESS_LOGIN_URL);
//		return success;
//	}
	
	@Bean
	public SimpleUrlAuthenticationFailureHandler failureHandler() {
		return new SimpleUrlAuthenticationFailureHandler();
	}

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	// used in dev for testing
//    @Bean
//    public MyPasswordEncoder passwordEncoder() {
//        return new MyPasswordEncoder();
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	throws Exception {
		auth.authenticationProvider(authenticationProvider());

//			.jdbcAuthentication()
//			.dataSource(dataSource)
//			.authoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY)
//			.passwordEncoder(new BCryptPasswordEncoder());
//			
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 
	    http.csrf().disable()
		    .exceptionHandling()
		    .authenticationEntryPoint(restAuthenticationEntryPoint)
		    .and()
		    .authorizeRequests()
//            .anyRequest().authenticated()
            .and()
	            .formLogin()
		            .permitAll()
		            .successHandler(successHandler())
		            .failureHandler(failureHandler())
            .and()
	            .logout()
//	                .invalidateHttpSession(true)
//	                .clearAuthentication(true)
            ;
	}
}
