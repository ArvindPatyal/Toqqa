package com.toqqa.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AgentRegistration {

	@NotNull
	private MultipartFile agentDocuments;

	@NotNull
	private MultipartFile idProof;

}
