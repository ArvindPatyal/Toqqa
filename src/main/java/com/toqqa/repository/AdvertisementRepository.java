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

	Page<Advertisement> findByUserAndIsDeleted(Pageable pageRequest, User user,Boolean isDeleted);
	
	List<Advertisement> findByUserAndIsActiveAndIsDeleted(User user,Boolean isActive,Boolean isDeleted);

	Page<Advertisement> findByIsDeleted(Pageable pageable,Boolean isDeleted);
}
