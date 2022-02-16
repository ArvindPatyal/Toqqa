package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Getter
@Setter
@NoArgsConstructor
public class AgentSignUp {

	@NotNull
	private MultipartFile agentDocuments;

	@NotNull
	private MultipartFile idProof;

	@NotNull
	@NotBlank
	private String userId;
}
