package com.projects.taskservice.repository;

import com.projects.taskservice.entity.Task;
import com.projects.taskservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignee(User assignee);

}