package com.projects.taskservice.service;

import com.projects.taskservice.entity.Task;
import com.projects.taskservice.entity.TaskDTO;
import com.projects.taskservice.entity.User;
import com.projects.taskservice.exceptions.TaskNotFoundException;
import com.projects.taskservice.exceptions.UnauthorizedAccessException;
import com.projects.taskservice.repository.TaskRepository;
import com.projects.taskservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks= taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean assignTaskToEmployee(Long taskId, Long employeeId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<User> employeeOptional = userRepository.findById(employeeId);

        if (taskOptional.isPresent() && employeeOptional.isPresent()) {
            Task task = taskOptional.get();
            User employee = employeeOptional.get();

            task.setAssignee(employee);
            taskRepository.save(task);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void markTaskAsComplete(Long taskId, String username) {
        User user = userRepository.findByUsername(username);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (task.getAssignee() == null || !task.getAssignee().equals(user)) {
            throw new UnauthorizedAccessException("You are not authorized to mark this task as complete");
        }
        task.setCompleted(true);
        taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> getTasksByUsername(String username) {
        User user = userRepository.findByUsername(username);
        List<Task> tasks = taskRepository.findByAssignee(user);
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        dto.setTaskAssignee(task.getAssignee().getUsername());
        return dto;
    }
}
