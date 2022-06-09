package com.toqqa.repository;

import com.toqqa.domain.Favourite;
import com.toqqa.domain.FavouriteSme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteSmeRepository extends JpaRepository<FavouriteSme, String> {
//    List<FavouriteSme> findByFavourite(Favourite favourite);

    void deleteBySmeIdAndFavourite(String smeId, Favourite favourite);
}
