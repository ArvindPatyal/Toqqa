package com.toqqa.controller;

import com.toqqa.bo.PaginationBo;
import com.toqqa.bo.ProductBo;
import com.toqqa.payload.AddProduct;
import com.toqqa.payload.FileUpload;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.payload.UpdateProduct;
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
    ProductService productService;

    @ApiOperation(value = "Add Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/addProduct")
    public Response<ProductBo> addProduct(@ModelAttribute @Valid AddProduct addProduct) {
        log.info("Inside controller add product");
        return new Response<ProductBo>(this.productService.addProduct(addProduct), "success");
    }

    @ApiOperation(value = "Update Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PutMapping("/updateProduct")
    public Response<ProductBo> updateProduct(@ModelAttribute @Valid UpdateProduct updateProduct) {
        log.info("Inside controller update product");
        return new Response<ProductBo>(this.productService.updateProduct(updateProduct), "success");
    }

    @ApiOperation(value = "Returns Product data by given id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/fetchProduct/{id}")
    public Response<ProductBo> fetchProduct(@PathVariable("id") @Valid String id) {
        log.info("Inside controller fetch product");
        return new Response<ProductBo>(this.productService.fetchProduct(id), "success");
    }

    @ApiOperation(value = "fetch Product List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/fetchProductList")
    public ListResponseWithCount<ProductBo> fetchProductList(@RequestBody @Valid PaginationBo paginationbo) {
        log.info("Inside controller fetch Product List");
        return this.productService.fetchProductList(paginationbo);

	}
        @DeleteMapping("/delete/{id}")
        public Response<?> deleteProduct (@PathVariable("id") @Valid String id){
            log.info("Inside controller fetch product");
            this.productService.deleteProduct(id);
            return new Response<Boolean>(true, "deleted successfully");
        }
        
        @DeleteMapping("/deleteattachment/{id}")
    	public Response<?> deleteAttachment(@PathVariable("id") @Valid String id) {
    		log.info("Inside controller delete attachment");
    		this.productService.deleteAttachment(id);
    		return new Response<Boolean>(true, "deleted successfully");
    	}
        
        @ApiOperation(value = "Update Product Image")
        @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
                @ApiResponse(code = 400, message = "Bad Request!")})
        @PutMapping("/updateProductImage")
        public Response<String> updateProductImage(@ModelAttribute @Valid FileUpload file) {
            log.info("Inside controller update product");
            return this.productService.updateProductImage(file);
        }
    }
}
