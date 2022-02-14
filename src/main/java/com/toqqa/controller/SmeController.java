package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.SmeBo;
import com.toqqa.payload.SmeSignUp;
import com.toqqa.service.SmeService;

@RestController
@RequestMapping("api/sme")
public class SmeController {
	@Autowired
	private SmeService smeService;
	
	@PostMapping("/addSme")
	public SmeBo addSme(@RequestBody @Valid SmeSignUp smeSignUp) {
		return this.smeService.addSme(smeSignUp);
	}

}
