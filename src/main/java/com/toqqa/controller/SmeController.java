package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.SmeBo;
import com.toqqa.payload.SmeSignUp;
import com.toqqa.service.SmeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/sme")
public class SmeController {
	@Autowired
	private SmeService smeService;

	@ApiOperation(value = "Sme registration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping("/addSme")
	public SmeBo addSme(@ModelAttribute @Valid SmeSignUp smeSignUp) {
		return this.smeService.addSme(smeSignUp);
	}

}
