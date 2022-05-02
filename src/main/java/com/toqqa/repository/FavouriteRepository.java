package com.toqqa.repository;

import com.toqqa.domain.Favourite;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, String> {
    Favourite findByUser(User currentUser);
}

