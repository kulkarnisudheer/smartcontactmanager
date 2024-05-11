
//<!-- 22 -->

package com.smartcontactproject.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactproject.entity.Contact;
import com.smartcontactproject.entity.User;

public interface ContactReposit extends JpaRepository<Contact, Integer> {

	@Query("select c from Contact c where c.user.id =:userId")
	public Page<Contact> getcontactsbyusername(@Param("userId") 
	int  userid,Pageable  pageable);
	
//	public List<Contact> findByNameContainingAndUser(String name,User user) ;
	
}
//<!-- 22 -->
