package com.toqqa.controller;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;
import com.toqqa.payload.Response;
import com.toqqa.service.DeliveryAddressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/address")
public class DeliveryAddressController {

	@Autowired
	DeliveryAddressService deliveryAddressService;

	@ApiOperation(value = "Add Address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/addAddress")
	public Response<DeliveryAddressBo> createAddress(
			@RequestBody @Valid DeliveryAddressPayload deliveryAddressPayload) {
		log.info("Inside controller add address");
		return new Response<DeliveryAddressBo>(this.deliveryAddressService.createAddress(deliveryAddressPayload),
				"success");
	}

	@ApiOperation(value = "Update Address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/updateAddress")
	public Response<DeliveryAddressBo> updateAddress(@RequestBody @Valid DeliveryAddressUpdate deliveryAddressUpdate) {
		log.info("Inside controller update address");
		return new Response<DeliveryAddressBo>(this.deliveryAddressService.updateAddress(deliveryAddressUpdate),
				"success");
	}

	@ApiOperation(value = "Returns Address data by given id")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request")})
	@GetMapping("/fetchAddress/{id}")
	public Response<DeliveryAddressBo> fetchAddress(@PathVariable("id") @Valid String id) {
		log.info("Inside controller fetch user");
		return new Response<>(this.deliveryAddressService.fetchAddress(id), "");
	}

	@DeleteMapping("/deleteAddress/{id}")
	public Response<?> deleteAddress(@PathVariable("id") @Valid String id) {
		log.info("Inside controller delete address");
		this.deliveryAddressService.deleteAddress(id);
		return new Response<Boolean>(true, "Address deleted successfully");
	}


	@ApiOperation(value = "fetch complete address list for a specific user")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
	@GetMapping("/fetchAddressList")
	public Response fetchAddressList() {
		log.info("Inside Controller Fetch address list");

		return this.deliveryAddressService.fetchAddressList();
	}

	@ApiOperation(value = "update Current Address ")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
	@PutMapping("/current/{addressId}")
	public Response currentAddress(@PathVariable @Valid String addressId) {
		log.info("Inside Controller current address update");
		return this.deliveryAddressService.currentOrder(addressId);
	}


}
