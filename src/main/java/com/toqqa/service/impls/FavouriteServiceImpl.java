package com.toqqa.service.impls;

import com.toqqa.bo.FavouriteSmeBo;
import com.toqqa.bo.SmeBo;
import com.toqqa.domain.Favourite;
import com.toqqa.domain.FavouriteSme;
import com.toqqa.domain.Sme;
import com.toqqa.exception.BadRequestException;
import com.toqqa.payload.FavouriteSmePayload;
import com.toqqa.payload.ListResponse;
import com.toqqa.payload.Response;
import com.toqqa.repository.FavouriteRepository;
import com.toqqa.repository.FavouriteSmeRepository;
import com.toqqa.repository.SmeRepository;
import com.toqqa.service.AuthenticationService;
import com.toqqa.service.FavouriteService;
import com.toqqa.service.SmeService;
import com.toqqa.util.Helper;
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
    private AuthenticationService authenticationService;
    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private FavouriteSmeRepository favouriteSmeRepository;
    @Autowired
    private SmeRepository smeRepository;
    @Autowired
    private SmeService smeService;
    @Autowired
    private Helper helper;


    @Override
    public Response addFavouriteSme(FavouriteSmePayload favouriteSmePayload) {
        log.info("Inside Service addFavouriteSme");
        Sme sme = this.smeRepository.findByUserId(favouriteSmePayload.getProductUserId());
        if (sme != null) {
            Favourite favourite = favouriteRepository.findByUser(authenticationService.currentUser());

            if (favourite == null) {
                favourite = new Favourite();
                favourite.setUser(authenticationService.currentUser());
            } else {
                boolean isExists = favourite.getFavouriteSmes().stream().anyMatch(favouriteSme -> favouriteSme.getSmeId().equals(sme.getId()));
                if (isExists) {
                    this.removeFavoriteSme(favouriteSmePayload.getProductUserId());
                    return new Response(true, "removed from favourites Successfully");
                }

            }
            favouriteRepository.saveAndFlush(favourite);
            favourite.setFavouriteSmes(this.persistSmes(sme, favourite));
            return new Response(true, "Added to favourites Successfully");
        } else {
            throw new BadRequestException("Enter a valid SmeId");
        }
    }

    @Override
    public ListResponse<SmeBo> fetchFavoriteList() {
        log.info("Inside Service fetchFavoriteList");
        Favourite favourite = this.favouriteRepository.findByUser(this.authenticationService.currentUser());
        if (favourite == null) {
            return new ListResponse<>(null, "favourites not found");
        }
        ListResponse<SmeBo> smeList = this.smeService.fetchSmeListWithoutPagination();

        List<SmeBo> favoriteSmes = new ArrayList<>();
        smeList.getData().stream().forEach(smeBo -> {
            if (this.isFavSme(smeBo, favourite)) {
                smeBo.setBusinessLogo(this.helper.prepareResource(smeBo.getBusinessLogo()));
                favoriteSmes.add(smeBo);
            }
        });
        return new ListResponse<>(favoriteSmes, "favourite smes fetched");
    }

    @Override
    public Boolean isFavSme(SmeBo smeBo, Favourite favourite) {
        log.info("Inside Service isFavSme");
        if (favourite != null && this.helper.notNullAndHavingData(favourite.getFavouriteSmes())) {
            return favourite.getFavouriteSmes().stream().anyMatch(favouriteSme -> favouriteSme.getSmeId().equals(smeBo.getId()));
        } else {
            return false;
        }
    }

    private List<FavouriteSme> persistSmes(Sme sme, Favourite favourite) {
        log.info("Inside Service persistSmes");
        FavouriteSme favouriteSme = new FavouriteSme();
        favouriteSme.setSmeId(sme.getId());
        favouriteSme.setSme(sme);
        favouriteSme.setFavourite(favourite);
        favouriteSme = favouriteSmeRepository.saveAndFlush(favouriteSme);
        return Arrays.asList(favouriteSme);
    }


    private List<FavouriteSmeBo> fetchFavouriteSme(Favourite favourite) {
        log.info("Inside Service fetchFavouriteSme");
        List<FavouriteSme> favouriteSmes = favouriteSmeRepository.findByFavourite(favourite);
        List<FavouriteSmeBo> favouriteSmeBoList = new ArrayList<>();
        favouriteSmes.forEach(sme -> favouriteSmeBoList.add(new FavouriteSmeBo(sme)));
        return favouriteSmeBoList;
    }


    @Override
    public void removeFavoriteSme(String productUserId) {
        log.info("Inside Service removeFavoriteSme");
        Favourite favourite = favouriteRepository.findByUser(authenticationService.currentUser());
        Sme sme = this.smeRepository.findByUserId(productUserId);
        if (favourite != null && this.helper.notNullAndHavingData(favourite.getFavouriteSmes())) {
            favouriteSmeRepository.deleteBySmeIdAndFavourite(sme.getId(), favourite);
            favourite.getFavouriteSmes().removeIf(favouriteSme -> favouriteSme.getSmeId().equals(sme.getId()));
            if (favourite.getFavouriteSmes().size() <= 0) {
                this.favouriteRepository.delete(favourite);
            }
        }
    }
}
