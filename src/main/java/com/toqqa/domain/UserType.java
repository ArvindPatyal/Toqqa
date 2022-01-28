package com.toqqa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table
public class UserType {
	
    @Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")	
	private String Id;
	
	private String userType;
	
	@ManyToMany
	@JoinTable(name = "user_userType",joinColumns= @JoinColumn(name = "userType_id"),
	inverseJoinColumns = @JoinColumn(name = "user_id"))
	private final List<User> users = new ArrayList<>();
}
