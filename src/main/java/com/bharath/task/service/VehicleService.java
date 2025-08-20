package com.bharath.task.service;

import com.bharath.task.entity.Dealer;
import com.bharath.task.entity.Vehicle;
import com.bharath.task.repository.DealerRepository;
import com.bharath.task.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle createVehicle(Vehicle vehicle, Long dealerId) {
        Dealer dealer = dealerRepository.findById(dealerId)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found with id: " + dealerId));
        vehicle.setDealer(dealer);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));

        existingVehicle.setModel(vehicleDetails.getModel());
        existingVehicle.setPrice(vehicleDetails.getPrice());
        existingVehicle.setStatus(vehicleDetails.getStatus());
        // The dealer associated with the vehicle is usually not changed.

        return vehicleRepository.save(existingVehicle);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new EntityNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    // Service method for the special requirement
    public List<Vehicle> getVehiclesOfPremiumDealers() {
        return vehicleRepository.findByDealerSubscriptionType(Dealer.SubscriptionType.PREMIUM);
    }
}