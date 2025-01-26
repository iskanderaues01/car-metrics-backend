package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.UserAny;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<UserAny, Long> {

    Optional<UserAny> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<UserAny> findByEmail(String email);

    Boolean existsByEmail(String email);

}
