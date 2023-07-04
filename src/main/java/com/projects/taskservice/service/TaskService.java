package com.projects.taskservice.service;

import com.projects.taskservice.entity.Task;
import com.projects.taskservice.entity.TaskDTO;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    List<TaskDTO> getAllTasks();

    Boolean assignTaskToEmployee(Long taskId, Long employeeId);

    void markTaskAsComplete(Long taskId, String username);

    List<TaskDTO> getTasksByUsername(String username);
}
