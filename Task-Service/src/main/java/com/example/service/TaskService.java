package com.example.service;

import com.example.entity.Task;
import com.example.repository.TaskRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    public TaskService(TaskRepository taskRepository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.taskRepository = taskRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    @Retry(name = "userService")
    public Task createTask(Task task) {
        // System Design: Fault Tolerance - Circuit breaker and retries
        String userServiceUrl = "http://user-service/users/" + task.getUserId();
        restTemplate.getForObject(userServiceUrl, Object.class);

        // System Design: Async Processing - Event-driven with RabbitMQ
        rabbitTemplate.convertAndSend("taskQueue", "Task created: " + task.getTitle());
        return taskRepository.save(task);
    }

    public Task fallback(Task task, Throwable t) {
        // System Design: Fault Tolerance - Fallback for unavailable user service
        throw new RuntimeException("User service unavailable: " + t.getMessage());
    }
}