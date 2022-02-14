package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AgentSignUp {
	
	@NotEmpty
	private MultipartFile agentDocuments;
	
	@NotEmpty
	private MultipartFile idProof;
	
	@NotNull
	@NotBlank
	private String userId;
}