package com.yqwireless.gxd.entity;

public class Product {

	private String phonenum;
	private String location;

	public Product() {
	};

	public Product(String phonenum, String location) {
		this.phonenum = phonenum;
		this.location = location;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
