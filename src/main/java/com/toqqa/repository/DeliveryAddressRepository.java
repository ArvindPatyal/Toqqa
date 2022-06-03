package com.toqqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.toqqa.domain.DeliveryAddress;
import com.toqqa.domain.User;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, String> {

	void deleteById(String id);

	List<DeliveryAddress> findByUser_Id(String id);

	@Query("select d from DeliveryAddress d where d.isCurrentAddress=TRUE and d.user=:user")
	Optional<DeliveryAddress> findCurrentDelAddress( @Param("user") User user);

}
