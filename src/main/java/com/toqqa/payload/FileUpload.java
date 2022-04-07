package com.toqqa.payload;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FileUpload {

	@NotNull
	@NotEmpty
	private String id;

	@NotNull
	private List<MultipartFile> images;
}
