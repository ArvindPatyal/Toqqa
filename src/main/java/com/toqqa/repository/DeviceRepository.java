package com.toqqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.toqqa.domain.Device;
import com.toqqa.domain.User;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
	
	Optional<Device> findByTokenAndUser(String token, User user);
	
	List<Device> findAllByUser(User user);


}
