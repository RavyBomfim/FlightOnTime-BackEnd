package com.flightontime.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightontime.api.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
