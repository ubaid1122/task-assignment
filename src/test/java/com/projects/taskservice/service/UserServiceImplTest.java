package com.projects.taskservice.service;

import com.projects.taskservice.entity.User;
import com.projects.taskservice.repository.UserRepository;
import com.projects.taskservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        // Create an instance of UserServiceImpl and set the mocked dependencies
        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder, jwtUtil);

        // Create a user and set it as the expected result
        User expectedUser = new User();
        expectedUser.setUsername("john");
        expectedUser.setPassword("password123");

        // Define the behavior of the mocked PasswordEncoder
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        // Define the behavior of the mocked UserRepository
        when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);

        // Call the registerUser method
        User actualUser = userService.registerUser(expectedUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }

}

