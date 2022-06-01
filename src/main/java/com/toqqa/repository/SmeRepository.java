package com.toqqa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.bo.OrderInfoBo;
import com.toqqa.domain.Sme;
import com.toqqa.payload.ToggleOrdersStatus;

@Repository
public interface SmeRepository extends JpaRepository<Sme, String> {

	Sme findByUserId(String userId);

//    Page<Sme> findByIsDeleted(Pageable pageable, Boolean isDeleted);

	List<Sme> findByIsDeleted(Boolean isDeleted);

}
