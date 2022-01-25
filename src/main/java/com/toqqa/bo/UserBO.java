package com.toqqa.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

public class UserBO {

	@Size(max = 1024)
	@NotNull
	@NotBlank
	private String firstName;
	
	@Size(max = 1024)
	@NotNull
	@NotBlank
	private String lastName;
	
	@Nullable
	private String email;
}
