package com.projects.taskservice.controller;

import com.projects.taskservice.entity.*;
import com.projects.taskservice.exceptions.TaskNotFoundException;
import com.projects.taskservice.exceptions.UnauthorizedAccessException;
import com.projects.taskservice.service.TaskService;
import com.projects.taskservice.service.UserService;
import com.projects.taskservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    private final TaskService taskService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, JwtUtil jwtUtil,
                          TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) throws AccessDeniedException {
        // Set the completed status to false for a new task
        task.setCompleted(false);

        // Save the task in the database
        return taskService.createTask(task);
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/tasks/{taskId}/assign/{employeeId}")
    public ResponseEntity<String> assignTask(@PathVariable Long taskId, @PathVariable Long employeeId) {
        Boolean assigned = taskService.assignTaskToEmployee(taskId, employeeId);
        if (assigned) {
            return ResponseEntity.ok("Task assigned successfully.");
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/tasks/{taskId}/complete")
    public ResponseEntity<String> markTaskAsComplete(
            @PathVariable("taskId") Long taskId, Principal principal) {
        try {
            String username = principal.getName();
            taskService.markTaskAsComplete(taskId, username);
            return ResponseEntity.ok("Task marked as complete");
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/myTasks")
    public List<TaskDTO> getMyTasks(Principal principal) {
        String username = principal.getName();
        return taskService.getTasksByUsername(username);
    }
}
