package com.toqqa.bo;

import javax.validation.constraints.NotNull;

import com.toqqa.domain.Attachment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentBo {

	private String id;

	@NotNull
	private String location;

	@NotNull
	private String fileType;

	@NotNull
	private String fileName;

	@NotNull
	private String mimeType;

	public AttachmentBo(Attachment attach) {
		this.id = attach.getId();
		this.location = attach.getLocation();
		this.fileType = attach.getFileType();
		this.fileName = attach.getFileName();
		this.mimeType = attach.getMimeType();
	}

}
