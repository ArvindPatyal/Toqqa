package com.toqqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.service.SmeService;

@RestController
@RequestMapping("api/sme")
public class SmeController {
	@Autowired
	private SmeService smeService;
}
