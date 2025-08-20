package com.bharath.task.repository;

import com.bharath.task.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
    // This method is crucial for Spring Security to find a user by their email
    Optional<Dealer> findByEmail(String email);
}