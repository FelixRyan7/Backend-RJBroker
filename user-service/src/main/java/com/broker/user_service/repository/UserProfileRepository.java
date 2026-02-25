package com.broker.user_service.repository;

import com.broker.user_service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    @Query("SELECT u.fullName FROM UserProfile u WHERE u.user.id = :userId")
    Optional<String> findFullNameByUserId(@Param("userId") Long userId);


}
