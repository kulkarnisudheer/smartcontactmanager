package com.smartcontactproject.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

	
	@Bean
	public UserDetailsService  getDetailsService() {
		
		
		return new UserService();
	}
	
	@Bean
	public BCryptPasswordEncoder  getPasswordEncoder() {
		 return new  BCryptPasswordEncoder();
		
	}
	
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setUserDetailsService(getDetailsService());
		dap.setPasswordEncoder(getPasswordEncoder());
		
		return dap;
	}
	 
	

	
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain  filterChain(HttpSecurity http) throws Exception{
		
		http.
		authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
		
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/**").permitAll().and()
		.formLogin().loginPage("/signin")
		.loginProcessingUrl("/dologin") 
		.defaultSuccessUrl("/user/view")
		.and().csrf(c -> c.disable());
		
		return  http.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
