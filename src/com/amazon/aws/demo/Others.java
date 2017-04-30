package com.amazon.aws.demo;

import java.io.Serializable;

public class Others implements Serializable {	
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String number;
	public Others(String name,String number)
	{
		this.name=name;
		this.number=number;
	}
	public Others(final String name) {
		
		  this.name=name;
		  this.number=null;
		  
		}
		
		public String getName() {
			return this.name;
		}
		public String getNumber() {
			return this.number;
		}

}
