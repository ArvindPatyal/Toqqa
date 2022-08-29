package com.toqqa.controller;

import com.toqqa.bo.FileBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.dto.UpdateSequenceNumberDTO;
import com.toqqa.payload.*;
import com.toqqa.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Add Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/addProduct")
    public Response<ProductBo> addProduct(@ModelAttribute @Valid AddProduct addProduct) {
        log.info("Invoked:: ProductController:: addProduct");
        return new Response<ProductBo>(this.productService.addProduct(addProduct), "success");
    }

    @ApiOperation(value = "Update Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PutMapping("/updateProduct")
    public Response<ProductBo> updateProduct(@ModelAttribute @Valid UpdateProduct updateProduct) {
        log.info("Invoked:: ProductController:: updateProduct");
        return new Response<ProductBo>(this.productService.updateProduct(updateProduct), "success");
    }

    @ApiOperation(value = "Returns Product data by given id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/fetchProduct/{id}")
    public Response<ProductBo> fetchProduct(@PathVariable("id") @Valid String id) {
        log.info("Invoked:: ProductController:: fetchProduct");
        return new Response<ProductBo>(this.productService.fetchProduct(id), "success");
    }

    @ApiOperation(value = "fetch Product List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/fetchProductList")
    public ListResponseWithCount<ProductBo> fetchProductList(@RequestBody @Valid ListProductRequest paginationbo) {
        log.info("Invoked:: ProductController:: fetchProductList");
        return this.productService.fetchProductList(paginationbo);

    }

    @ApiOperation(value = " Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
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
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PutMapping("/updateProductImage")
    public ListResponse<FileBo> updateProductImage(@ModelAttribute @Valid FileUpload file) {
        log.info("Invoked:: ProductController:: updateProductImage");
        return this.productService.updateProductImage(file);
    }

  /*  @ApiOperation(value = "Search for product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Bad request")})
    @PostMapping("/search")
    public ListResponseWithCount<ProductBo> searchProducts(@RequestBody @Valid PaginationBo bo) {
        log.info("Invoked:: ProductController:: searchProducts");
        return this.productService.searchProducts(bo);
    }*/

    @ApiOperation(value = "Update Product sequence")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/update_sequence")
    public Response<Boolean> updateSequence(@RequestBody @Valid UpdateSequenceNumberDTO dto) {
        log.info("Invoked:: ProductController:: updateProductImage");
        return new Response<Boolean>(this.productService.updateSequenceNumber(dto), "sequence updated successfully!!");
    }

}