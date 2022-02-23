package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.ProductBo;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.Response;
import com.toqqa.payload.UpdateProduct;
import com.toqqa.service.ProductService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@ApiOperation(value = "Add Product")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/addProduct")
	public Response<ProductBo> addProduct(@ModelAttribute @Valid AddProduct addProduct) {

		return new Response<ProductBo>(this.productService.addProduct(addProduct), "success");
	}

	@ApiOperation(value = "Update Product")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/updateProduct")
	public Response<ProductBo> updateProduct(@ModelAttribute @Valid UpdateProduct updateProduct) {
		return new Response<ProductBo>(this.productService.updateProduct(updateProduct), "success");
	}

	@ApiOperation(value = "Returns Product data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchProduct/{id}")
	public Response<ProductBo> fetchProduct(@PathVariable("id") @Valid String id) {
		return new Response<ProductBo>(this.productService.fetchProduct(id), "success");
	}

}
