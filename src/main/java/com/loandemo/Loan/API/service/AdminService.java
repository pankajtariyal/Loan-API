package com.loandemo.Loan.API.service;

import com.loandemo.Loan.API.dto.user.UserResponse;
import com.loandemo.Loan.API.modul.User;
import com.loandemo.Loan.API.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;
    public AdminService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers(){
        List<User> userList = userRepository.findAll();

        List<UserResponse> responseList = userList.stream().map(user->{
            return UserResponse.builder()
                    .id(user.getUser_id())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .active(user.isActive())
                    .created_at(user.getCreated_at().toString())
                    .created_by(user.getCreated_by())
                    .updated_at(user.getUpdated_at().toString())
                    .updated_at(user.getUpdated_by())
                    .build();
        }).collect(Collectors.toList());
        return responseList;
    }
}
