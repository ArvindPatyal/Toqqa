package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.SmeBo;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.payload.Response;
import com.toqqa.payload.SmeUpdate;
import com.toqqa.service.SmeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/sme")
public class SmeController {
	@Autowired
	private SmeService smeService;

	@ApiOperation(value = "sme updation")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/smeupdate")
	public Response<SmeBo> smeUpdate(@ModelAttribute @Valid SmeUpdate smeUpdate) {
		log.info("Invoked:: SmeController:: smeUpdate");
		return new Response<SmeBo>(this.smeService.smeUpdate(smeUpdate), "success");
	}

	@ApiOperation(value = "Returns Sme data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchSme/{id}")
	public Response<SmeBo> fetchSme(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: SmeController:: fetchSme");
		return new Response<SmeBo>(this.smeService.fetchSme(id), "success");
	}

	@ApiOperation(value = "fetch filtered product list of Sme")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/productsList")
	public ListResponseWithCount smeProductsFilter(@RequestBody ProductRequestFilter productRequestFilter) {
		log.info("Invoked:: SmeController:: smeProductsFilter");
		return smeService.fetchProductsList(productRequestFilter);
	}

}
