package com.toqqa.controller;

import javax.validation.Valid;

import com.toqqa.dto.UpdateSequenceNumberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.FileUpload;
import com.toqqa.payload.ListProductRequest;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.payload.ToggleStatus;
import com.toqqa.payload.UpdateProduct;
import com.toqqa.service.ProductService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@ApiOperation(value = "Add Product")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/addProduct")
	public Response<ProductBo> addProduct(@ModelAttribute @Valid AddProduct addProduct) {
		log.info("Invoked:: ProductController:: addProduct");
		return new Response<ProductBo>(this.productService.addProduct(addProduct), "success");
	}

	@ApiOperation(value = "Update Product")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/updateProduct")
	public Response<ProductBo> updateProduct(@ModelAttribute @Valid UpdateProduct updateProduct) {
		log.info("Invoked:: ProductController:: updateProduct");
		return new Response<ProductBo>(this.productService.updateProduct(updateProduct), "success");
	}

	@ApiOperation(value = "Returns Product data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchProduct/{id}")
	public Response<ProductBo> fetchProduct(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: ProductController:: fetchProduct");
		return new Response<ProductBo>(this.productService.fetchProduct(id), "success");
	}

	@ApiOperation(value = "fetch Product List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/fetchProductList")
	public ListResponseWithCount<ProductBo> fetchProductList(@RequestBody @Valid ListProductRequest paginationbo) {
		log.info("Invoked:: ProductController:: fetchProductList");
		return this.productService.fetchProductList(paginationbo);

	}

	@DeleteMapping("/delete/{id}")
	public Response<?> deleteProduct(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: ProductController:: deleteProduct");
		this.productService.deleteProduct(id);
		return new Response<Boolean>(true, "deleted successfully");
	}

	@ApiOperation(value = " Product")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/updateProductStatus")
	public Response<ProductBo> updateProductStatus(@RequestBody @Valid ToggleStatus request) {
		log.info("Invoked:: ProductController:: updateProductStatus");
		return new Response<ProductBo>(this.productService.updateProductStatus(request), "success");
	}

	@DeleteMapping("/deleteattachment/{id}")
	public Response<?> deleteAttachment(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: ProductController:: deleteAttachment");
		return new Response<Boolean>(this.productService.deleteAttachment(id), "product disabled successfully");
	}

	@ApiOperation(value = "Update Product Image")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/updateProductImage")
	public ListResponse<FileBo> updateProductImage(@ModelAttribute @Valid FileUpload file) {
		log.info("Invoked:: ProductController:: updateProductImage");
		return this.productService.updateProductImage(file);
	}

	@ApiOperation(value = "Search for product")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad request") })
	@PostMapping("/search")
	public ListResponseWithCount<ProductBo> searchProducts(@RequestBody PaginationBo bo) {
		log.info("Invoked:: ProductController:: searchProducts");
		return this.productService.searchProducts(bo);
	}

	@ApiOperation(value = "Update Product sequence")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/update_sequence")
	public Response<Boolean> updateSequence(@RequestBody @Valid UpdateSequenceNumberDTO dto) {
		log.info("Invoked:: ProductController:: updateProductImage");
		return new Response<Boolean>(this.productService.updateSequenceNumber(dto),"sequence updated successfully!!");
	}

}