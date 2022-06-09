package com.toqqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.ProductCategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.ProductCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;

	@ApiOperation(value = "Returns Category List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@GetMapping("/categories")
	public ListResponse<ProductCategoryBo> getProductCategoryList() {
		log.info("Invoked:: ProductCategoryController:: getProductCategoryList");
		return new ListResponse<ProductCategoryBo>(this.productCategoryService.getProductCategories(), "success");
	}

}
