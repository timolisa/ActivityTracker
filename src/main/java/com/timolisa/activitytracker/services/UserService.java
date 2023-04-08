package com.timolisa.activitytracker.services;

import com.timolisa.activitytracker.DTO.UserDTO;
import com.timolisa.activitytracker.entity.User;

public interface UserService {
    UserDTO findById(Long userId);
    void saveUser(UserDTO userDTO);
    User findByUsername(String username);
    User findByEmail(String email);
    UserDTO loginUser(UserDTO userDTO);
}
