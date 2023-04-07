package com.timolisa.activitytracker.services;

import com.timolisa.activitytracker.DTO.UserDTO;

public interface UserService {
    void saveUser(UserDTO userDTO);
    UserDTO getUserByUsername(String username);
}
