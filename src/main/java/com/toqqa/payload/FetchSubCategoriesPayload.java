package com.toqqa.payload;

import java.util.List;

import lombok.Data;

@Data
public class FetchSubCategoriesPayload {

	private List<String> categoryIds;
}
