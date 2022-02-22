package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgentRegistration {

	@NotNull
	private MultipartFile agentDocuments;

	@NotNull
	private MultipartFile idProof;

}
