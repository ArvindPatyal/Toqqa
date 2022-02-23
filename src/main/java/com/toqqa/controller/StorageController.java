package com.toqqa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.service.StorageService;

@RestController
@RequestMapping("/file")
public class StorageController {

	@Autowired
	private StorageService storageService;

	/*
	 * @PostMapping("/upload") public String uploadFile(@RequestParam(value =
	 * "file") MultipartFile file) { return storageService.uploadFile(file); }
	 */
}
