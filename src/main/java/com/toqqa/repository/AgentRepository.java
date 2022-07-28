package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Agent;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {

    Agent findByUserId(String userId);
    Optional<Agent> getByUserId(String userId);

}
