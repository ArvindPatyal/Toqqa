package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.service.AdvertisementService;
import com.toqqa.service.CustomerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private AdvertisementService advertisementService;

	@ApiOperation(value = "fetch product list for customer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/productList")
	public ListResponseWithCount productList(@RequestBody @Valid CustomerProductRequest bo) {
		log.info("Inside Controller customer productList");
		return customerService.productList(bo);
	}

	@ApiOperation(value = "fetchAds")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@GetMapping("/fetchTopAds")
	public ListResponse<AdvertisementBo> fetchTopActiveAdds() {
		log.info("Inside controller fetch top ads");
		return new ListResponse<>(this.advertisementService.fetchTopActiveAdds(), "");
	}

	/*
	 * @ApiOperation(value = "fetchAds")
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = ""),
	 * 
	 * @ApiResponse(code = 400, message = "Bad Request!") })
	 * 
	 * @GetMapping("/fetchTopAds") public ListResponse<ProductBo>
	 * fetchProductsByProductCategories() {
	 * log.info("Inside controller fetch top ads"); return new
	 * ListResponse<>(this..fetchProductsByProductCategories(),""); }
	 */
}
