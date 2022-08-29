package com.toqqa.controller;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.dto.NearbySmeRespDto;
import com.toqqa.payload.CustomerProductRequest;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.ProductRequestFilter;
import com.toqqa.service.AdvertisementService;
import com.toqqa.service.CustomerService;
import com.toqqa.service.SmeService;
import com.toqqa.util.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private SmeService smeService;


    @ApiOperation(value = "fetch product list for customer")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/products")
    public ListResponseWithCount productList(@RequestBody @Valid CustomerProductRequest customerProductRequest) {
        log.info("Invoked:: CustomerController:: Customer:: Fetch productList");
        return customerService.productList(customerProductRequest);
    }

    @ApiOperation(value = "fetchAds")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @GetMapping("/fetchTopAds")
    public ListResponse<AdvertisementBo> fetchTopActiveAdds() {
        log.info("Invoked:: CustomerController:: fetchTopActiveAdds");
        return new ListResponse<>(advertisementService.fetchTopActiveAdds(), "");
    }

    @ApiOperation(value = "fetch productsList of a Specific Sme")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @PostMapping("/fetchSmeProducts")
    public ListResponseWithCount fetchSmeProducts(@RequestBody ProductRequestFilter productRequestFilter) {
        log.info("Invoked CustomerController fetchSmeProducts");
        return customerService.smeProductList(productRequestFilter);
    }

    @ApiOperation(value = "getNearbySme")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "Bad Request!")})
    @GetMapping("/nearby-sme")
    public ListResponse<NearbySmeRespDto> getNearbySme() {
        log.info("Invoked :: CustomerController :: getNearbySme()");
        return doSuccessResponse(smeService.getNearbySme(), Constants.MSG_DATA_PROCESSED);
    }

}
