package com.toqqa.repository;

import com.toqqa.domain.NotificationHistory;
import com.toqqa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationHistory, String> {


    List<NotificationHistory> findByUserAndRole(User user, String notificationFor);
}
