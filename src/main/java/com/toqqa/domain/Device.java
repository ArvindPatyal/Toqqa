package com.toqqa.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class Device {
	
	    @Id
	    @GeneratedValue(generator = "uuid2")
	    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	    private String id;

		@Column(unique = true)
	    private String token;
	    
	    @ManyToOne
	    @JoinColumn(name ="user_id") 
	    private User user;
	    

}
