package com.toqqa.repository;


import com.toqqa.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, String> {

    Wishlist findByUser_Id(String userId);

}
