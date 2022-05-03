package com.toqqa.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.AdvertisementBo;
import com.toqqa.bo.PaginationBo;
import com.toqqa.payload.AdvertisementPayload;
import com.toqqa.payload.AdvertisementUpdate;
import com.toqqa.payload.ListResponseWithCount;
import com.toqqa.payload.Response;
import com.toqqa.payload.ToggleAdStatus;
import com.toqqa.service.AdvertisementService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

	@Autowired
	AdvertisementService advertisementService;

	@ApiOperation(value = "Create Advertisement")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/createAd")
	public Response<AdvertisementBo> createAd(@ModelAttribute @Valid AdvertisementPayload advertisementPayload) {
		log.info("Inside controller add order");
		return new Response<AdvertisementBo>(this.advertisementService.createAds(advertisementPayload), "success");
	}

	@ApiOperation(value = "Update Advertisement")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/updateAd")
	public Response<AdvertisementBo> createAd(@ModelAttribute @Valid AdvertisementUpdate advertisementUpdate) {
		log.info("Inside controller update Advertisement");
		return new Response<AdvertisementBo>(this.advertisementService.updateAd(advertisementUpdate), "success");
	}

	@DeleteMapping("/delete/{id}")
	public Response<?> deleteAdvertisement(@PathVariable("id") @Valid String id) {
		log.info("Inside controller delete product");
		this.advertisementService.deleteAd(id);
		return new Response<Boolean>(true, "success");
	}

	@ApiOperation(value = "Returns Advertisement data by given id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@GetMapping("/fetchAd/{id}")
	public Response<AdvertisementBo> fetchAd(@PathVariable("id") @Valid String id) {
		log.info("Inside controller fetch product");
		return new Response<AdvertisementBo>(this.advertisementService.fetchAd(id), "success");
	}

	@ApiOperation(value = "fetch Advertisement List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/fetchAdList")
	public ListResponseWithCount<AdvertisementBo> fetchAdvertisementList(
			@RequestBody @Valid PaginationBo paginationbo) {
		log.info("Inside controller fetch Advertisement List");
		return this.advertisementService.fetchAdvertisementList(paginationbo);
	}

	@ApiOperation(value = "updateClick")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PutMapping("/updateClicks/{id}")
	public Response<AdvertisementBo> updateClick(@PathVariable("id") @Valid String id) {
		log.info("Inside controller update clicks");
		return new Response<AdvertisementBo>(this.advertisementService.updateClick(id), "success");
	}

	@ApiOperation(value = "updateAdsStatus")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@PostMapping("/updateAdsStatus")
	public Response<AdvertisementBo> updateAdsStatus(@RequestBody @Valid ToggleAdStatus request) {
		log.info("Inside controller update ad status");
		return new Response<AdvertisementBo>(this.advertisementService.updateAdsStatus(request), "success");
	}

	/*@ApiOperation(value = "fetchTopAds")
	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
			@ApiResponse(code = 400, message = "Bad Request!") })
	@GetMapping("/fetchTopAds")
	public List<AdvertisementBo> fetchTopActiveAdds() {
		log.info("Inside controller fetch top ads");
		return this.advertisementService.fetchTopActiveAdds();
	}*/

//	@ApiOperation(value = "allotQueueNumber")
//	@ApiResponses(value = { @ApiResponse(code = 200, message = ""),
//			@ApiResponse(code = 400, message = "Bad Request!") })
//	@GetMapping("/queueNumber/{id}")
//	public List<AdvertisementBo> allotQueueNumber(@PathVariable("id") @Valid String id) {
//		log.info("Inside controller queueNumber");
//		return this.advertisementService.allotQueueNumber(id);
//		
//	}
}
