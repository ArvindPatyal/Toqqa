package com.toqqa.controller;

import com.toqqa.bo.ProductCategoryBo;
import com.toqqa.payload.ListResponse;
import com.toqqa.service.ProductCategoryService;
import com.toqqa.service.SmeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private SmeService smeService;

    @ApiOperation(value = "Returns Category List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @GetMapping("/categories")
    public ListResponse<ProductCategoryBo> getProductCategoryList() {
        log.info("Invoked:: ProductCategoryController:: getProductCategoryList");
        return new ListResponse<ProductCategoryBo>(this.productCategoryService.getProductCategories(), "success");
    }

    @ApiOperation(value = "Returns Category List for a seller")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @GetMapping("/categories/sme")
    public ListResponse smeProductCategories() {
        log.info("Invoked :: ProductCategoryController :: smeProductCategories()");
        return this.smeService.smeProductCategories();
    }


}
