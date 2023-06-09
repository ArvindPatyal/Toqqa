package com.toqqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Advertisement;
import com.toqqa.domain.User;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, String> {

	Page<Advertisement> findByUserAndIsDeleted(Pageable pageRequest, User user, Boolean isDeleted);

	List<Advertisement> findByUserAndIsActiveAndIsDeleted(User user, Boolean isActive, Boolean isDeleted);

	List<Advertisement> findByUserAndIsDeleted(User user, Boolean isDeleted);

	Page<Advertisement> findByIsDeleted(Pageable pageable, Boolean isDeleted);

	List<Advertisement> findTop10ByOrderByQueueDateAsc();

	Optional<Advertisement> findByProduct_IdAndIsDeleted(String id,Boolean isDeleted);

	List<Advertisement> findByOrderByQueueDateAsc();

	List<Advertisement> findByIsActiveOrderByQueueDateAsc(boolean isActive);

	List<Advertisement> findTop10ByIsActiveOrderByQueueDateAsc(boolean isActive);


}