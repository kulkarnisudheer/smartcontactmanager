package com.smartcontactproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactproject.entity.Contact;
import com.smartcontactproject.entity.User;

public interface UserReposit extends JpaRepository<User, Integer>{

	@Query( "select u from User  u where u.email =:email"  )
	public User getuser(@Param("email") String  email);
	
//	@Query("select c from Contact  c where c.cname =:name")
//	public Contact getcontact(@Param("cname") String name);
//	
	
	 
}
