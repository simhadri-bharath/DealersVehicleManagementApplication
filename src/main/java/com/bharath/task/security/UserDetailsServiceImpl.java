package com.bharath.task.security;

import com.bharath.task.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DealerRepository dealerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // We are using email as the username for authentication
        return dealerRepository.findByEmail(email)
                .map(dealer -> new User(
                        dealer.getEmail(),
                        dealer.getPassword(),
                        Collections.emptyList() // We are not using roles for now, but you could add them here
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Dealer not found with email: " + email));
    }
}