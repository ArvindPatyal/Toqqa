package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.ProductSubCategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.ProductSubCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/product")
public class ProductSubCategoryController {

	@Autowired
	private ProductSubCategoryService productSubCategoryService;

	@ApiOperation(value = "Get productSubCategory List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "sucess"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/productsubcategories/{id}")
	public ListResponse<ProductSubCategoryBo> getCategoryList(@PathVariable("id") @Valid String id) {
		return new ListResponse<ProductSubCategoryBo>(this.productSubCategoryService.getProductSubCategories(id),
				"success");
	}

}
