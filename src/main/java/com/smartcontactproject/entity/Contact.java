package com.smartcontactproject.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "scontact")
public class Contact {
	


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
private int cid; 

	
@NotBlank(message = "Name field is required !!")
@Size(min = 3,max = 15,message = "min 3 and max 15 characters are allowed !!")
private String cname;
private String cnickname;
private String cwork;
private String cemail;
private String cphoneno;
private String photo;
@Column(length = 10000)
private String cdescription;

@ManyToOne
private User user;

public Contact() {
	super();
	// TODO Auto-generated constructor stub
}
public int getCid() {
	return cid;
}
public void setCid(int cid) {
	this.cid = cid;
}
public String getCname() {
	return cname;
}
public void setCname(String cname) {
	this.cname = cname;
}
public String getCnickname() {
	return cnickname;
}
public void setCnickname(String cnickname) {
	this.cnickname = cnickname;
}
public String getCwork() {
	return cwork;
}
public void setCwork(String cwork) {
	this.cwork = cwork;
}
public String getCemail() {
	return cemail;
}
public void setCemail(String cemail) {
	this.cemail = cemail;
}
public String getCphoneno() {
	return cphoneno;
}
public void setCphoneno(String cphoneno) {
	this.cphoneno = cphoneno;
}


public String getPhoto() {
	return photo;
}
public void setPhoto(String photo) {
	this.photo = photo;
}



public String getCdescription() {
	return cdescription;
}
public void setCdescription(String cdescription) {
	this.cdescription = cdescription;
}

public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}

//
//public boolean equals(Object obj) {
//	return cid == ((Contact)obj).getCid();
//}



}
