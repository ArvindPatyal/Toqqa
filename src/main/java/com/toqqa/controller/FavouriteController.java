package com.toqqa.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toqqa.bo.SmeBo;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.service.FavouriteService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/favourites")
public class FavouriteController {

	@Autowired
	FavouriteService favouriteService;

	@ApiOperation(value = "add to favourites")
	@PostMapping("/add")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	public Response addFavourite(@RequestBody @Valid FavouriteSmePayload favouriteSmePayload) {
		log.info("Invoked:: FavouriteController:: addFavourite");
		return favouriteService.addFavouriteSme(favouriteSmePayload);
	}

	@ApiOperation(value = "to fetch favourite Sme")
	@GetMapping("/fetchFavoriteList")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	public ListResponse<SmeBo> fetchFavourite() {
		log.info("Invoked:: FavouriteController:: fetchFavourite");
		return favouriteService.fetchFavoriteList();
	}

	@ApiOperation(value = "delete a sme from favourite")
	@DeleteMapping("delete/{productUserId}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Bad Request") })
	public Response deleteFavouriteSme(@PathVariable("productUserId") @Valid String productUserId) {
		log.info("Invoked:: FavouriteController:: deleteFavouriteSme");
		favouriteService.removeFavoriteSme(productUserId);
		return new Response(true, "removed Successfully");
	}
}
