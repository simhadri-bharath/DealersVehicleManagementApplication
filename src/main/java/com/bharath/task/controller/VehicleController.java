package com.bharath.task.controller;

import com.bharath.task.entity.Vehicle;
import com.bharath.task.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    // Note: In a real app, you would get the dealerId from the authenticated principal (JWT)
    // For simplicity here, we are passing it as a request param.
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle, @RequestParam Long dealerId) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle, dealerId));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for the special requirement
    @GetMapping("/premium-dealers")
    public ResponseEntity<List<Vehicle>> getVehiclesFromPremiumDealers() {
        return ResponseEntity.ok(vehicleService.getVehiclesOfPremiumDealers());
    }
}