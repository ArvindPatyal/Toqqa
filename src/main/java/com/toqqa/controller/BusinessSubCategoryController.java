package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.SubCategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.BusinessSubCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/business")
public class BusinessSubCategoryController {
	@Autowired
	private BusinessSubCategoryService businessSubCategoryService;

	@ApiOperation(value = "Get SubCategory List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "sucess"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/subcategories/{id}")
	public ListResponse<SubCategoryBo> getCategoryList(@PathVariable("id") @Valid String id) {
		return new ListResponse<SubCategoryBo>(this.businessSubCategoryService.getSubCategories(id), "success");
	}
}