package com.projects.taskservice.controller;

import com.projects.taskservice.entity.LoginRequest;
import com.projects.taskservice.entity.LoginResponse;
import com.projects.taskservice.entity.User;
import com.projects.taskservice.service.TaskService;
import com.projects.taskservice.service.UserService;
import com.projects.taskservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, authenticationManager, userDetailsService, jwtUtil, taskService);
    }

    @Test
    public void testRegisterUser() {
        // Create a user
        User user = new User();
        user.setUsername("john");
        user.setPassword("password123");

        // Define the behavior of the mocked UserService
        when(userService.registerUser(user)).thenReturn(user);

        // Call the registerUser method
        User result = userController.registerUser(user);

        // Assert the result
        assertEquals(user, result);
    }

    @Test
    public void testLoginUser_Success() {
        // Create a LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("password123");

        // Define the behavior of the mocked AuthenticationManager
        // Assuming authentication is successful

        // Define the behavior of the mocked UserDetailsService
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginRequest.getUsername())).thenReturn(userDetails);

        // Define the behavior of the mocked JwtUtil
        String token = "jwtToken";
        when(jwtUtil.generateToken(userDetails.getUsername())).thenReturn(token);

        // Call the loginUser method
        ResponseEntity<?> response = userController.loginUser(loginRequest);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
