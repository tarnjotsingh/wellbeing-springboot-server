package com.reading.tvirdee.project.springbootdockermysql.service;

import com.reading.tvirdee.project.springbootdockermysql.domain.Users;
import com.reading.tvirdee.project.springbootdockermysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring security service for getting user details from
 * the database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepo;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Load by username, uses userRepo to find by username.
     *
     * @param username Username of the user to retrieve.
     * @return Spring security compatible UserDetails object.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepo.findByUsername(username);

        if(!user.isPresent())
            throw new UsernameNotFoundException("User with username " + username + " not found.");

        return new CustomUserDetails(user.get());
    }
}
