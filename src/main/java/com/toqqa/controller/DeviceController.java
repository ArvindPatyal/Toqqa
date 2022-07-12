package com.toqqa.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.toqqa.payload.Response;
import com.toqqa.service.impls.DeviceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/device")
@Slf4j
public class DeviceController {
	
	private DeviceService deviceService;
	
	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	@ApiOperation(value = "Delete Device")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@RequestMapping(value = "/{deviceToken}", method = RequestMethod.DELETE)
	public Response<?> delete(@PathVariable("deviceToken") String deviceToken) {
		log.info("Invoked :: DeviceController :: delete :: deviceToken ::"+ deviceToken);
		deviceService.deleteDevice(deviceToken);
		return new Response<Boolean>(true, "success");

	}

}
