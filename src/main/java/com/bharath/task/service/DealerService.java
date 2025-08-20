package com.bharath.task.service;

import com.bharath.task.dto.DealerRegistrationRequest;
import com.bharath.task.entity.Dealer;
import com.bharath.task.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealerService {

    private final DealerRepository dealerRepository;
    private final PasswordEncoder passwordEncoder;

    public Dealer registerDealer(DealerRegistrationRequest request) {
        if (dealerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Dealer with email " + request.getEmail() + " already exists.");
        }

        Dealer dealer = Dealer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Encode the password
                .subscriptionType(request.getSubscriptionType())
                .build();

        return dealerRepository.save(dealer);
    }

    public List<Dealer> getAllDealers() {
        return dealerRepository.findAll();
    }

    public Optional<Dealer> getDealerById(Long id) {
        return dealerRepository.findById(id);
    }
}