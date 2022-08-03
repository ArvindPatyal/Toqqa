package com.toqqa.repository;

import com.toqqa.domain.ResetToken;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, String> {
    Optional<ResetToken> findByToken(String token);
    void deleteAllByUser(User user);
}
