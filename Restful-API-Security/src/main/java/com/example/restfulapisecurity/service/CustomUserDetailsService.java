package com.example.restfulapisecurity.service;

import com.example.restfulapisecurity.model.User;
import com.example.restfulapisecurity.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 'UserDetailsService' Interface : It is responsible for loading user details from
 * the database based on the provided username (in this case, the email)
 *
 * 'CustomerUserDetailService' Class: Overall, this class serves as a bridge
 * between Spring Security's authentication mechanism and the application's user database.
 * It provides a way to load user details during the authentication process based on the username (email).
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        /**
         * Build UserDetails by using User.builder()
         */
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String [0]))
                        .build();

        return userDetails;
    }
}
