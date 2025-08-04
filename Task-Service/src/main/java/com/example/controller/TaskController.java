package com.example.controller;

import com.example.entity.Task;
import com.example.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')") // System Design: RBAC
    public Task createTask(@RequestBody Task task) {
        // System Design: Idempotency - Check for existing task
        return taskService.createTask(task);
    }
}