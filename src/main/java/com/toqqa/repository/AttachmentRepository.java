package com.toqqa.repository;

import com.toqqa.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {

    void deleteById(String id);

    Optional<Attachment> findByLocation(String location);
}
