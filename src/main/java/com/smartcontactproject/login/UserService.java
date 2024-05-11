package com.smartcontactproject.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.smartcontactproject.dao.UserReposit;
import com.smartcontactproject.entity.User;


@Component
public class UserService implements UserDetailsService {

	
	@Autowired
	  private UserReposit reposit;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user  = reposit.getuser(username);
		
		if(user == null) {  
			throw new UsernameNotFoundException("could not found user");
		}
		NormalDetails nd = new NormalDetails(user);
		return nd;
	}

}
