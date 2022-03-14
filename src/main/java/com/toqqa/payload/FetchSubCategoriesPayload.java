package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class FetchSubCategoriesPayload {
	
	@NotEmpty
	private List<String> categoryIds;
}
