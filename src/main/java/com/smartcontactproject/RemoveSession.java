package com.smartcontactproject;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class RemoveSession {
	
	
	public void removemessage() {
		
		try {
			
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
        
		}
		
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
