package com.toqqa.controller;

import com.toqqa.bo.CategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.BusinessCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/business")
public class BusinessCategoryController {
    @Autowired
    private BusinessCategoryService businessCategoryService;

    @GetMapping("/categories")
    public ListResponse<CategoryBo> getCategoryList(){
        return new ListResponse(this.businessCategoryService.getCategories(),"");
    }
}
