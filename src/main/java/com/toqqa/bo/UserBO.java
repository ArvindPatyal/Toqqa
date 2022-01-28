package com.toqqa.bo;

import com.toqqa.domain.User;

import lombok.Data;

@Data
public class UserBO {

	private String Id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phone;					
	
	private String houseNumber;
	
	private String street;
	
	private String city;
	
	private String postCode;
	
	private String state;
	
	private String country;
	
	private String agentId;	
	
	private boolean isDeleted;
	
	public void UserBo(User user)
	{
		this.Id=user.getId();
		this.firstName=user.getFirstName();
		this.lastName=user.getLastName();
		this.email=user.getEmail();
		this.phone=user.getPhone();		
		this.houseNumber=user.getHouseNumber();
		this.street=user.getStreet();
		this.city=user.getCity();
		this.postCode=user.getPostCode();
		this.state=user.getState();
		this.country=user.getCountry();
		this.agentId=user.getAgentId();						
	}
	
	
}
