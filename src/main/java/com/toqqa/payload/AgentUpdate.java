package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AgentUpdate {

	@NotNull
	private MultipartFile agentDocuments;

	@NotNull
	private MultipartFile idProof;

	@NotNull
	private String agentId;
}
