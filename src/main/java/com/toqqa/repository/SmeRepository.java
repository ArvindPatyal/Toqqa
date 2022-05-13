package com.toqqa.repository;

import com.toqqa.domain.Sme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmeRepository extends JpaRepository<Sme, String> {

    Sme findByUserId(String userId);

//    Page<Sme> findByIsDeleted(Pageable pageable, Boolean isDeleted);

    List<Sme> findByIsDeleted(Boolean isDeleted);
}
