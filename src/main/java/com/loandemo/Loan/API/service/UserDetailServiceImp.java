package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService} used by Spring Security
 * for authenticating users during login.
 *
 * <p>This service loads user-specific data from the database and converts
 * it into a Spring Security compatible {@link UserDetails} object.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Fetch user details from database using username</li>
 *     <li>Convert application {@link User} entity into {@link UserDetails}</li>
 *     <li>Provide user credentials and roles to Spring Security</li>
 * </ul>
 *
 * <p>Authentication Flow:</p>
 * <pre>
 * User enters username & password →
 * Spring Security calls loadUserByUsername() →
 * Fetch user from DB →
 * Convert to UserDetails →
 * Authentication Manager validates password
 * </pre>
 *
 * <p>This class is automatically used by Spring Security during authentication.</p>
 *
 * @author Abhishek Tadiwal
 */
@Service
public class UserDetailServiceImp implements UserDetailsService {

    /**
     * Repository for accessing user data from the database.
     */
    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    /**
     * Loads user details by username.
     *
     * <p>This method is invoked by Spring Security during authentication.</p>
     *
     * <p>Steps performed:</p>
     * <ul>
     *     <li>Search user in database using username</li>
     *     <li>If user exists → map to {@link UserDetails}</li>
     *     <li>If not found → throw {@link UsernameNotFoundException}</li>
     * </ul>
     *
     * <p>The returned {@link UserDetails} contains:</p>
     * <ul>
     *     <li>Username</li>
     *     <li>Encrypted password</li>
     *     <li>User roles</li>
     * </ul>
     *
     * @param username the username identifying the user
     * @return {@link UserDetails} object used by Spring Security
     * @throws UsernameNotFoundException if user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        }
        else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
