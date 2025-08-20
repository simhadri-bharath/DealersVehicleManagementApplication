package com.bharath.task.repository;

import com.bharath.task.entity.Dealer;
import com.bharath.task.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Custom query to find all vehicles where the dealer's subscription type is PREMIUM
    List<Vehicle> findByDealerSubscriptionType(Dealer.SubscriptionType subscriptionType);
}