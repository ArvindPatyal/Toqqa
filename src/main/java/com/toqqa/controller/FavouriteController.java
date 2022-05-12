package com.toqqa.controller;

import com.toqqa.bo.SmeBo;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.service.FavouriteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/favourites")
public class FavouriteController {

    @Autowired
    FavouriteService favouriteService;

    @ApiOperation(value = "add to favourites")
    @PostMapping("/add")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response addFavourite(@RequestBody @Valid FavouriteSmePayload favouriteSmePayload) {
        log.info("Inside controller addFavourite");
        return favouriteService.addFavouriteSme(favouriteSmePayload);
    }

    @ApiOperation(value = "to fetch favourite Sme")
    @GetMapping("/fetchFavoriteList")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public ListResponse<SmeBo> fetchFavourite() {
        log.info("Inside Controller fetchFavourite");
        return favouriteService.fetchFavoriteList();
    }

    @ApiOperation(value = "delete a sme from favourite")
    @DeleteMapping("delete/{productUserId}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response deleteFavouriteSme(@PathVariable("productUserId") @Valid String productUserId) {
        favouriteService.removeFavoriteSme(productUserId);
        return new Response(true, "removed Successfully");
    }
}
