package com.timolisa.activitytracker.servicesImpl;

import com.timolisa.activitytracker.DTO.UserDTO;
import com.timolisa.activitytracker.entity.User;
import com.timolisa.activitytracker.repository.UserRepository;
import com.timolisa.activitytracker.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        // https://www.baeldung.com/mockito-annotations
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    public void shouldSaveUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userMapper.toUser(userDTO)).thenReturn(user);

        userService.saveUser(userDTO);
        verify(userRepository).save(user);
    }

    @Test
    void ShouldFindByUsername() {
        String username = "abc";
        User user = new User();

        when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsername(username);
        assertEquals(user, result);
    }

    @Test
    void shouldLoginUser() {
        String username = "testuser";
        String password = "1234";
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);

        User user = new User();
        user.setPassword(password);

        when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));

        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.loginUser(userDTO);

        assertEquals(userDTO, result);
    }

    @Test
    void findByEmail() {
        String email = "example@gmail.com";
        User user = new User();

        when(userRepository.findUserByEmail(email))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertEquals(user ,result);
    }
}