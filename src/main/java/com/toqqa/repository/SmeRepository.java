package com.toqqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Sme;

@Repository
public interface SmeRepository extends JpaRepository<Sme, String> {

    Sme findByUserId(String userId);
    Optional<Sme> getByUserId(String userId);
    
    @Query("select s from Sme s where s.isDeleted=:isDeleted")
    List<Sme> findAll(@Param("isDeleted") Boolean isDeleted);
    
    
    
}
