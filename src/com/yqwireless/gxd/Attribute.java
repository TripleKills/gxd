package com.yqwireless.gxd;

public class Attribute {

	private String phonenum;
	private String location;

	public Attribute() {
	};

	public Attribute(String phonenum, String location) {
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
