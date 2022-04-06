package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {

	void deleteById(String id);
	
	
}
