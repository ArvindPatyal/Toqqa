package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Sme;
@Repository
public interface SmeRepository extends JpaRepository<Sme, String> {
	


}
