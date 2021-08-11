package com.example.springModel;

import java.io.Serializable;

public class Address  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String type;
	private String city;
	private String country;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [type=" + type + ", city=" + city + ", country=" + country + "]";
	}
	
}
