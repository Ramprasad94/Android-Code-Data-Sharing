package com.amazon.aws.demo;

import java.io.Serializable;

public class Users implements Serializable {	
	private static final long serialVersionUID = 1L;

	private final String uname;
//	private final String password;
	//private final String email;
	private final String phno;
	
	public Users( final String uname,final String phno ) {
		this.uname = uname;
		//this.password = password;
		//this.email=email;
		this.phno=phno;
	}
	
	public Users(final String uname) {
		
	  this.uname=uname;
	  //this.password=null;
	//  this.email=null;
	  this.phno=null;
	}
	
	public String getUsername() {
		System.out.println("users"+uname);
		return this.uname;
	}
/*	public String getPassword() {
		System.out.println("users"+password);
		return this.password;
	}*/
	
/*	public String getEmail() {
		return this.email;
	}*/
	public String getPhno() {
		System.out.println("users"+phno);
		return this.phno;
	}
}
