package com.toqqa.bo;

import com.toqqa.domain.User;

import lombok.Data;

@Data
public class UserBo {

	private String id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;					
	
	private String address;
	
	private String city;
	
	private String postCode;
	
	private String state;
	
	private String country;
	
	private String agentId;	
	
	private Boolean isDeleted;
	
	public UserBo(User user)
	{
		this.id=user.getId();
		this.firstName=user.getFirstName();
		this.lastName=user.getLastName();
		this.email=user.getEmail();
		this.phone=user.getPhone();		
		this.address=user.getAddress();
		this.city=user.getCity();
		this.postCode=user.getPostCode();
		this.state=user.getState();
		this.country=user.getCountry();
		this.agentId=user.getAgentId();
		this.isDeleted=user.getIsDeleted();
	}
	
	
}