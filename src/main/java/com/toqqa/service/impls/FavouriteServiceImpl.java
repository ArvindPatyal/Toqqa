package com.toqqa.service.impls;

import com.toqqa.bo.FavouriteBo;
import com.toqqa.bo.FavouriteSmeBo;
import com.toqqa.domain.Favourite;
import com.toqqa.domain.FavouriteSme;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.Response;
import com.toqqa.repository.FavouriteRepository;
import com.toqqa.repository.FavouriteSmeRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.FavouriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
public class FavouriteServiceImpl implements FavouriteService {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    FavouriteRepository favouriteRepository;
    @Autowired
    FavouriteSmeRepository favouriteSmeRepository;
    @Autowired
    SmeRepository smeRepository;


    @Override
    public Response addSme(FavouriteSmePayload favouriteSmePayload) {
        log.info("Inside Service addSme");
        Favourite favourite = favouriteRepository.findByUser(authenticationService.currentUser());
        if (favourite == null) {
            favourite = new Favourite();
            favourite.setUser(authenticationService.currentUser());
        } else {
            boolean isExists = favourite.getFavouriteSmes().stream().anyMatch(favouriteSme -> favouriteSme.getSmeId().equals(favouriteSmePayload.getSmeId()));
            if (isExists) {
                this.removeSme(favouriteSmePayload.getSmeId());
                return new Response(true, "Added to favourites Successfully");
            }

        }
        favouriteRepository.saveAndFlush(favourite);
        favourite.setFavouriteSmes(this.persistSmes(favouriteSmePayload, favourite));
        return new Response(true, "Added to favourites Successfully");
    }


    public List<FavouriteSme> persistSmes(FavouriteSmePayload favouriteSmePayload, Favourite favourite) {
        log.info("Inside Service persistSmes");
        FavouriteSme favouriteSme = new FavouriteSme();
        favouriteSme.setSmeId(favouriteSmePayload.getSmeId());
        favouriteSme.setSme(smeRepository.getById(favouriteSmePayload.getSmeId()));
        favouriteSme.setFavourite(favourite);
        favouriteSme = favouriteSmeRepository.saveAndFlush(favouriteSme);
        return Arrays.asList(favouriteSme);
    }


    public List<FavouriteSmeBo> fetchFavouriteSme(Favourite favourite) {
        log.info("Inside Service fetchFavouriteSme");
        List<FavouriteSme> favouriteSmes = favouriteSmeRepository.findByFavourite(favourite);
        List<FavouriteSmeBo> favouriteSmeBoList = new ArrayList<>();
        favouriteSmes.forEach(sme -> favouriteSmeBoList.add(new FavouriteSmeBo(sme)));
        return favouriteSmeBoList;
    }

    @Override
    public FavouriteBo fetchFavourite() {
        log.info("Inside Service fetchfavourite");
        Favourite favourite = favouriteRepository.findByUser(authenticationService.currentUser());
        if (favourite != null) {
            return new FavouriteBo(favourite, this.fetchFavouriteSme(favourite));
        }
        throw new BadRequestException("no such wishlist found");
    }

    @Override
    public Response removeSme(String smeId) {
        log.info("Inside Service removeSme");
        String favouriteId = favouriteRepository.findByUser(authenticationService.currentUser()).getId();
        favouriteSmeRepository.deleteBySmeIdAndFavourite_Id(smeId, favouriteId);
        return new Response(true, "removed Successfully");
    }
}
