package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.SubCategoryBo;
import com.toqqa.payload.FetchSubCategoriesPayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.BusinessSubCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/business")
public class BusinessSubCategoryController {
	@Autowired
	private BusinessSubCategoryService businessSubCategoryService;

	@ApiOperation(value = "Get SubCategory List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/subcategories")
	public ListResponse<SubCategoryBo> getCategoryList(@RequestBody @Valid FetchSubCategoriesPayload getSubPayload) {
		log.info("Invoked:: BusinessSubCategoryController:: getCategoryList");
		return new ListResponse<SubCategoryBo>(this.businessSubCategoryService.getSubCategories(getSubPayload),
				"success");
	}
}