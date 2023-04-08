package com.timolisa.activitytracker.servicesImpl;

import com.timolisa.activitytracker.DTO.UserDTO;
import com.timolisa.activitytracker.entity.User;
import com.timolisa.activitytracker.repository.UserRepository;
import com.timolisa.activitytracker.services.UserService;
import com.timolisa.activitytracker.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@Transactional
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
        log.info("User to save: {}", user);
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        return userOptional.orElse(null);
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        User existingUser = findByUsername(username);

        if (existingUser != null
                && existingUser.getPassword().equals(userDTO.getPassword())) {
            return userMapper.toUserDTO(existingUser);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.orElse(null);
    }
}
