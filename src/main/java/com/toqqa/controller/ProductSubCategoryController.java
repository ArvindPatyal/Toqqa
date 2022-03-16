package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.ProductSubCategoryBo;
import com.toqqa.payload.FetchSubCategoriesPayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.ProductSubCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductSubCategoryController {

	@Autowired
	private ProductSubCategoryService productSubCategoryService;

	@ApiOperation(value = "Get productSubCategory List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "sucess"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/productsubcategories")
	public ListResponse<ProductSubCategoryBo> getCategoryList(@RequestBody @Valid FetchSubCategoriesPayload getSub) {			
		log.info("Inside controller get category list");
		return new ListResponse<ProductSubCategoryBo>(this.productSubCategoryService.getProductSubCategories(getSub),"success");				
	}

}