package com.smartcontactproject.login;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactproject.controller.Message;
import com.smartcontactproject.dao.ContactReposit;
import com.smartcontactproject.dao.UserReposit;
import com.smartcontactproject.entity.Contact;
import com.smartcontactproject.entity.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/user")
public class Logincontroller {
	
	@Autowired
	private UserReposit reposit;
	
	@Autowired
	private ContactReposit conreposit;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	//adding common data 
	@ModelAttribute
public void commondata(Model m,Principal p) {
	
	 
String username = p.getName();

System.out.println("UserName : "+ username);

User user = reposit.getuser(username);

System.out.println("User Details : "+ user);

m.addAttribute("detail", user);
}
	
	
	//
	
	@GetMapping("/view")
	public String afterloginform(Model m,Principal p) {
        
		m.addAttribute("title","Userview");

		return "normaluser/userview";
	}


//addcontact handler
	
@GetMapping("/addcontact")
public String addContact(Model m) {
	m.addAttribute("title","Add Contact");
	m.addAttribute("contact",new Contact()); 
	
	return "normaluser/addcontact";
}


//proccessing adding contact

@PostMapping("/process")
public String addcontact(@Valid @ModelAttribute("contact") Contact contact,BindingResult result,@RequestParam("myphoto") MultipartFile file,
		 Model m, Principal p,HttpSession session) {
	
	try {
	String contactname = p.getName();
	User user = reposit.getuser(contactname);
	
	

	
	//uploding file 
	
	if(file.isEmpty()) { 
		
		System.out.println("file is empty");
		//<!-- 24 -->

		contact.setPhoto("contact.png");
		//<!-- 24 -->

	}
	else {
		contact.setPhoto(file.getOriginalFilename());
		File  f = new ClassPathResource("static/image").getFile();
		
		Path path = Paths.get(f.getAbsolutePath()+File.separator+file.getOriginalFilename());
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING );
		System.out.println("image is uploaded");
	} 
	

	if(result.hasErrors()) {
		System.out.println("Error"+ result.toString());
		m.addAttribute("ct",contact);
		return "normaluser/addcontact";
	}
	
contact.setUser(user);
	
	user.getCon().add(contact);
	reposit.save(user);
	
	
	
	System.out.println("added to database");
	
	System.out.println("contact details :"+ contact);
	
	//success message
	
	session.setAttribute("message", new Message("Contact is successfully added", "success"));
	
	
	
	}
	catch (Exception e) {
		
		
		System.out.println("error"+e.getMessage());
		
		//error message
		
		session.setAttribute("message", new Message("Some thing went wrong !!", "danger"));

	}
	
	return "normaluser/addcontact"; 
}


////<!-- 22 -->
//<!-- 23 -->
// view contacts handler

@GetMapping("/showcontacts/{page}")
public String showcontacts( @PathVariable("page")Integer page ,Model m ,Principal p) {
	
	m.addAttribute("title","view contacts");
	
	/// show the contacts list in view contacts
//	String getcontacts = p.getName();
//	
//	
//User user = reposit.getuser(getcontacts);
//	
//	List<Contact> con = user.getCon();
//	
//	m.addAttribute("contacts",con);
//	
	
	
	String username = p.getName();
	
	User user = reposit.getuser(username);
	
	Pageable  pageable = PageRequest.of(page, 5);
	
	Page <Contact> contacts = conreposit.getcontactsbyusername(user.getId(),pageable);
	
	m.addAttribute("contacts",contacts);
	
	m.addAttribute("currentpage",page);
	m.addAttribute("totalpages",contacts.getTotalPages());
	

	
	return "normaluser/showcontacts";
}
////<!-- 22 -->

//<!-- 23 -->


//<!-- 24 -->

// showing particular contact details

@GetMapping("/contact/{cid}")
public String  showContactDetail(@PathVariable("cid") Integer cid,Model m,Principal p) {
	
	
	System.out.println("cid" + cid);
	Optional<Contact> contactid = conreposit.findById(cid);
	
	Contact getcontact = contactid.get();
	
	//25
	//get only contacts who are logedin 
	String username = p.getName();

	User getuser = reposit.getuser(username);
	
	if(getuser.getId() == getcontact.getUser().getId() ) {
		
		m.addAttribute("contactid",getcontact);
		m.addAttribute("title",getcontact.getCname());
	}
	 
	//25
	
	
	
	return "normaluser/contactdetail";
	
}

//<!-- 24 -->


 
//26
//deleting contact

@GetMapping("/deletecontact/{cid}")
public String deletecontact(@PathVariable("cid")Integer cid,Model m,HttpSession session,Principal p) {
	
	Optional<Contact> Idoptional  = conreposit.findById(cid);
	Contact contact = Idoptional.get();
	
//	contact.setUser(null);
//	
//	
//	conreposit.deleteById(contact.getCid());
//	
	
	User user = reposit.getuser(p.getName());
//	user.getCon().remove(contact);
//	
//	
//	reposit.save(user);
	
	
	 if(user.getId() == contact.getUser().getId()) {
		 
		 user.getCon().removeIf(c -> c.getCid()==contact.getCid());
			conreposit.delete(contact);
			reposit.save(user);
	 }
	
	 
	 //deleting contact photo  after deleting the contact
	 String image = contact.getPhoto();
	 
	 if(image != null && !image.isEmpty()) {
		 
		 try {
		File f  = new  ClassPathResource("static/image").getFile();
		Path path =  Paths.get(f.getAbsoluteFile() + File.separator +image);
		Files.deleteIfExists(path);
		System.out.println("file is deleted successfully");
			 
	 }
		 catch (Exception e) {
			 e.printStackTrace();
		}
	 }
	 
	 
	
	session.setAttribute("message",new Message("Deleted contact successfully", "success"));
	
	return "redirect:/user/showcontacts/0";
}

//26



//27
//update contact details



@PostMapping("/update-contact-detail/{cid}")
public String updateform( @PathVariable("cid")Integer cid,Model m) {
	
	m.addAttribute("title","update contact");
	
	Optional<Contact> byId = conreposit.findById(cid);
	Contact contact = byId.get();
	m.addAttribute("contact",contact);
	
	
	
	return "normaluser/updatecontact";
	
}
//27


// update contact details in database
//28-`
@PostMapping("/process-update")
public String updatecontactdetails(@ModelAttribute 	Contact contact,  
 @RequestParam("myphoto") MultipartFile file ,Model m,HttpSession session,Principal p	) {
	try {
		
		Optional<Contact> byId = conreposit.findById(contact.getCid());
		Contact oldcontact = byId.get();
		String image = oldcontact.getPhoto();
		if(!file.isEmpty()) {
			
//			// old photo delete
			// old photo deleting old photo this two methods are work 

//		File deletefile = new ClassPathResource("static/image").getFile();
//		File fi = new File(deletefile, oldcontact.getPhoto());
//			fi.delete();
//				System.out.println("file is deleted");
			
			// old photo deleting old photo this two methods are work 
			
		File deletefile = new ClassPathResource("static/image").getFile();
Path pa = Paths.get(deletefile.getAbsolutePath()+File.separator+image);
		
Files.deleteIfExists(pa);

			//update photo
				
				File  f1 = new ClassPathResource("static/image").getFile();
				
				Path path1 = Paths.get(f1.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING );
				contact.setPhoto(file.getOriginalFilename());
		}
		else {
			
			contact.setPhoto(oldcontact.getPhoto());		
		}
		
		User user = reposit.getuser(p.getName());
		
		contact.setUser(user);	
		
		conreposit.save(contact);
		
		session.setAttribute("message" ,new Message("Your contact is updated", "success"));
	}
	catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	 
	
	
	return "redirect:/user/contact/"+contact.getCid();
}

//profile handler

@GetMapping("/profile")
public String profile(Model m,Principal p) {
	m.addAttribute("title", "your profile");
	User user = reposit.getuser(p.getName());
	user.setImageurl("download1234.png");
	m.addAttribute("user", user);
	
	
	return "normaluser/yourprofile";
}



//settings handler
@GetMapping("/settings")
public String settings(Model  m) {
	
	m.addAttribute("title","settings");
	return "normaluser/settings";
}


//change password

@PostMapping("/change-password")
public String changepassword(@RequestParam("old password") String oldpassword,
		@RequestParam("new password") String newpassword,Principal p,HttpSession session) {
	
	System.out.println(oldpassword);
	User user = reposit.getuser(p.getName());
	
	
	if(passwordEncoder.matches(oldpassword,user.getPassword() )) {
		
		// change password
		
		user.setPassword(passwordEncoder.encode(newpassword));
		reposit.save(user);
		session.setAttribute("message", new Message("password changed successfully", "success"));
		 
	}
	else {
		session.setAttribute("message", new Message("Invalid old password", "danger"));

		return "normaluser/settings";
	}
	
return "normaluser/userview";
}






   


























}