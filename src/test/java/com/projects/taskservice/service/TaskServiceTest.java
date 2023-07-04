package com.projects.taskservice.service;

import com.projects.taskservice.entity.Task;
import com.projects.taskservice.entity.TaskDTO;
import com.projects.taskservice.entity.User;
import com.projects.taskservice.repository.TaskRepository;
import com.projects.taskservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private TaskServiceImpl taskService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(taskRepository, userRepository);
    }

    @Test
    public void testGetAllTasks() {
        // Create a mock TaskRepository
        TaskRepository taskRepository = mock(TaskRepository.class);

        UserRepository userRepository = mock(UserRepository.class);

        // Create an instance of TaskService and set the mocked TaskRepository as a dependency
        TaskService taskService = new TaskServiceImpl(taskRepository, userRepository);

        // Create a list of tasks and set it as the expected result of the mocked TaskRepository
        List<Task> expectedTasks = new ArrayList<>();
        User user = new User();
        user.setUsername("johnDoe");

        Task task1 = new Task();
        task1.setAssignee(user);
        Task task2 = new Task();
        task2.setAssignee(user);
        expectedTasks.add(task1);
        expectedTasks.add(task2);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Call the getAllTasks method
        List<TaskDTO> actualTasks = taskService.getAllTasks();

        // Assert the result
        assertEquals(expectedTasks.size(), actualTasks.size());
        // Add more assertions for the expected result
    }

    @Test
    public void testAssignTaskToEmployee() {
        // Create a task and an employee
        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description 1");
        User employee = new User();
        employee.setUsername("john");

        // Define the behavior of the mocked TaskRepository
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // Define the behavior of the mocked UserRepository
        when(userRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        // Call the assignTaskToEmployee method
        boolean result = taskService.assignTaskToEmployee(task.getId(), employee.getId());

        // Assert the result
        assertEquals(true, result);
        assertEquals(employee, task.getAssignee());
        // Add more assertions for the expected result
    }
}
