package com.toqqa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Advertisement;
import com.toqqa.domain.User;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {

	Page<Advertisement> findByUser(Pageable pageRequest, User user);
	
	List<Advertisement> findByUserAndIsActive(User user,Boolean isActive);

}
