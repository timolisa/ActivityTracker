package com.timolisa.activitytracker.servicesImpl;

import com.timolisa.activitytracker.DTO.UserDTO;
import com.timolisa.activitytracker.entity.User;
import com.timolisa.activitytracker.exceptions.UserNotFoundException;
import com.timolisa.activitytracker.repository.UserRepository;
import com.timolisa.activitytracker.services.UserService;
import com.timolisa.activitytracker.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        userRepository.save(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        return userMapper.toUserDTO(userOptional.orElseThrow(() -> new UserNotFoundException(username)));
    }
}
