package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.DeliveryAddressBo;
import com.toqqa.payload.DeliveryAddressPayload;
import com.toqqa.payload.DeliveryAddressUpdate;
import com.toqqa.payload.Response;
import com.toqqa.service.DeliveryAddressService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

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
		log.info("Invoked:: DeliveryAddressController:: createAddress");
		return new Response<DeliveryAddressBo>(this.deliveryAddressService.createAddress(deliveryAddressPayload),
				"success");
	}

	@ApiOperation(value = "Update Address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/updateAddress")
	public Response<DeliveryAddressBo> updateAddress(@RequestBody @Valid DeliveryAddressUpdate deliveryAddressUpdate) {
		log.info("Invoked:: DeliveryAddressController:: updateAddress");
		return new Response<DeliveryAddressBo>(this.deliveryAddressService.updateAddress(deliveryAddressUpdate),
				"success");
	}

	@ApiOperation(value = "Returns Address data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchAddress/{id}")
	public Response<DeliveryAddressBo> fetchAddress(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: DeliveryAddressController:: fetchAddress");
		return new Response<>(this.deliveryAddressService.fetchAddress(id), "");
	}

	@DeleteMapping("/deleteAddress/{id}")
	public Response<?> deleteAddress(@PathVariable("id") @Valid String id) {
		log.info("Invoked:: DeliveryAddressController:: deleteAddress");
		this.deliveryAddressService.deleteAddress(id);
		return new Response<Boolean>(true, "Address deleted successfully");
	}

	@ApiOperation(value = "fetch complete address list for a specific user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchAddressList")
	public Response fetchAddressList() {
		log.info("Invoked:: DeliveryAddressController:: fetchAddressList");

		return this.deliveryAddressService.fetchAddressList();
	}

	@ApiOperation(value = "update Current Address ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@PutMapping("/current/{addressId}")
	public Response currentAddress(@PathVariable @Valid String addressId) {
		log.info("Invoked:: DeliveryAddressController:: currentAddress");
		return this.deliveryAddressService.currentAddress(addressId);
	}

}
