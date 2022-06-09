package com.toqqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.CategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.BusinessCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/business")
public class BusinessCategoryController {
	@Autowired
	private BusinessCategoryService businessCategoryService;

	@ApiOperation(value = "Returns Category List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@GetMapping("/categories")
	public ListResponse<CategoryBo> getCategoryList() {
		log.info("Invoked:: BusinessCategoryController:: getCategoryList");
		return new ListResponse<CategoryBo>(this.businessCategoryService.getCategories(), "success");
	}
}
