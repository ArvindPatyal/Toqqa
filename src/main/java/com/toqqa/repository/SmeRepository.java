package com.toqqa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Sme;

import java.util.List;

@Repository
public interface SmeRepository extends JpaRepository<Sme, String> {

    Sme findByUserId(String userId);

    Page<Sme> findByIsDeleted(Pageable pageable, Boolean isDeleted);
}
