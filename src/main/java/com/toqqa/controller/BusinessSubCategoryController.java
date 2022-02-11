package com.toqqa.controller;

import com.toqqa.bo.CategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.BusinessCategoryService;
import com.toqqa.service.BusinessSubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/business")
public class BusinessSubCategoryController {
    @Autowired
    private BusinessSubCategoryService businessSubCategoryService;

    @GetMapping("/subcategories/{id}")
    public ListResponse<CategoryBo> getCategoryList(@PathVariable("id") @Valid String id){
        return new ListResponse(this.businessSubCategoryService.getSubCategories(id),"");
    }
}
