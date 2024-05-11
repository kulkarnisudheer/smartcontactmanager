package com.smartcontactproject.controller;

import java.security.Principal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactproject.dao.ContactReposit;
import com.smartcontactproject.dao.UserReposit;
import com.smartcontactproject.entity.Contact;
import com.smartcontactproject.entity.User;
import com.smartcontactproject.service.Emailsevice;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@org.springframework.stereotype.Controller
public class Controller {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	UserReposit userreposit;
	
	
	@RequestMapping("/")
	public String home(Model m) {
		m.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title","Register - Smart Contact Manager");
		m.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping("/do register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult result1,
	@RequestParam(value = "agreement",defaultValue  = "false") boolean agreement, Model m,HttpSession session) {
		
		
		try {
			
		if(!agreement) {
			System.out.println("you have not  agreed the terms and conditions");
			throw new Exception("you have not  agreed the terms and conditions");
		}
		
		if(result1.hasErrors()) {
			System.out.println("error"+ result1.toString());
			m.addAttribute("user",user);
			return "signup";
		}
	
		
		user.setEnabled(true);
		user.setRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		System.out.println("agreement"+ agreement);
		System.out.println("user" +user);
		
		
 		User  result =  userreposit.save(user);
		
		
		 
		m.addAttribute("us", new User());
		session.setAttribute("message", new Message("successfully register","alert-success" ));

		return "signup";
		  
	}
	catch (Exception e) { 
		e.printStackTrace();
		m.addAttribute("user",user);
		session.setAttribute("message", new Message("some thing went wrong !!" + e.getMessage(),"alert-danger" ));
		return "signup";
		
	
	} 
	}
	
	//login page
	@GetMapping("/signin")
	public String login(Model m) {
		
		m.addAttribute("title","Login");
		
		return "login";
	}
	
	
	
	//forgot password

	@GetMapping("/forgot")
	public String forgotPassword() {
		
		return "forgot-password";
	}
	
	
	Random random = new Random(1000);
	
	@Autowired
	 private Emailsevice emailsevice;
	
	@PostMapping("/send-otp")
	public String sendotp(@RequestParam("email") String email,HttpSession session) {
		
		//generating otp
		
		
		int otp = random.nextInt(88888);
		
		
		System.out.println("otp " + otp);
		
//		otp send to email
		
		String subject = "sending OTP from Smart Contact Manager";
		String message = "<div style ='border 1px solid grey ; padding:20px'> "
				+"Your One Time Password (OTP) "
				+ "for Generating New Password on SMC is "
				+ "<h4>"	
				+ "<b>"
				+ otp
				
				+".</b>"
	
				+"</h4>"
		
				
				+"</div>";
				
	
		
		String to = email;
		
		
		boolean flag = emailsevice.sendemail(subject, message, to);
		
		if(flag) {
			  
			
			session.setAttribute("myotp", otp);
			session.setAttribute("email",email);
			return "verify-otp"; 
		}
		else {
			session.setAttribute("message", new Message("check your email id you have entered", "alert-danger"));
			return "forgot-password"; 
		}
		

	}
	
	
	
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyotp(@RequestParam("otp") int otp,HttpSession session) {
		
		
		int myotp = (int) session.getAttribute("myotp");
		
		
		String  email = (String)session.getAttribute("email");
		
		if(myotp == otp) {
			//password change form
			
			User user = userreposit.getuser(email);
			
			if(user == null) {
				//send error message
				session.setAttribute("message", new Message("No user with this email ", "danger"));
				return "forgot-password"; 
				
			}
			else {
				//send change password form
				
				return "password-change";
			}
			
		
		}
		else {
			session.setAttribute("message",new Message("you have entered wrong OTP", "danger") );
			
			return "verify-otp";
		}
		
		
	}
	
	
	
	// password change
	
	@PostMapping("/password-change")
	public String passwordchange(@RequestParam("newpassword") String newpassword,HttpSession session) {
		
		String  email = (String)session.getAttribute("email");
		
		User user = userreposit.getuser(email);
		
		user.setPassword(passwordEncoder.encode(newpassword));
		
		userreposit.save(user);
		 
		session.setAttribute("message", new Message("your password is changed successfully", "alert-success"));
		
		return "redirect:/signin"; 
		
	}
	
	
	
//	
//	//search
//	
//	@Autowired
//	
//	private ContactReposit conreposit;
//	 @GetMapping("/search/{query}")
//	public ResponseEntity<?>search(@PathVariable("query") String query ,Principal p){
//		
//		 
//		 User user = userreposit.getuser(p.getName());
//		 List<Contact> contacts = conreposit.findByNameContainingAndUser(query,user);
//		 
//		 
//		 return ResponseEntity.ok(contacts);
//	}
//	
	
	

}
