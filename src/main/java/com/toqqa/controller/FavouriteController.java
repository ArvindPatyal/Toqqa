package com.toqqa.controller;

import com.toqqa.bo.FavouriteBo;
import com.toqqa.payload.FavouriteSmePayload;
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
@RequestMapping("api/user/favourites")
public class FavouriteController {

    @Autowired
    FavouriteService favouriteService;

    @ApiOperation(value = "add to favourites")
    @PostMapping("/add")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response addFavourite(@RequestBody @Valid FavouriteSmePayload favouriteSmePayload) {
        log.info("Inside controller addFavourite");
        return favouriteService.addSme(favouriteSmePayload);
    }

    @ApiOperation(value = "to fetch favourite Sme")
    @GetMapping("/fetch")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public FavouriteBo fetchFavourite() {
        log.info("Inside Controller fetchFavourite");
        return favouriteService.fetchFavourite();
    }

    @ApiOperation(value = "delete a sme from favourite")
    @DeleteMapping("delete/{smeId}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 400, message = "Bad Request")})
    public Response deleteFavouriteSme(@PathVariable("smeId") @Valid String smeId) {
        return favouriteService.removeSme(smeId);
    }
}
