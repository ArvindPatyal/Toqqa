package com.toqqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {

}
