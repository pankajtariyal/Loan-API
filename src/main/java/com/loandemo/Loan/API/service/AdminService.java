package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for admin-related operations.
 *
 * <p>This service provides functionality accessible to administrators,
 * such as retrieving user details and managing user data.</p>
 *
 * <p>Main Responsibilities:</p>
 * <ul>
 *     <li>Fetch all users from the database</li>
 *     <li>Convert {@link User} entities into {@link UserResponse} DTOs</li>
 * </ul>
 *
 * <p>This class follows a clean separation of concerns by converting
 * database entities into response DTOs before returning data to controllers.</p>
 *
 * <p>Example Usage:</p>
 * <pre>
 * List&lt;UserResponse&gt; users = adminService.getAllUsers();
 * </pre>
 *
 * @author Abhishek
 */
@Service
public class AdminService {

    /**
     * Repository for performing database operations on {@link User}.
     */
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection.
     *
     * @param userRepository repository for user data access
     */
    public AdminService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database and converts them
     * into a list of {@link UserResponse} DTOs.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *     <li>Fetch all user entities using {@link UserRepository}</li>
     *     <li>Transform each {@link User} into {@link UserResponse}</li>
     *     <li>Return the list of DTOs</li>
     * </ol>
     *
     * @return list of {@link UserResponse} containing user details
     */
    public List<UserResponse> getAllUsers(){
        List<User> userList = userRepository.findAll();

        return userList.stream().map(user -> {
            return UserResponse.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .active(user.isActive())
                    .created_at(user.getCreated_at().toString())
                    .created_by(user.getCreated_by())
                    .updated_at(user.getUpdated_at().toString())
                    .updated_by(user.getUpdated_by())
                    .build();
        }).collect(Collectors.toList());
    }
}
