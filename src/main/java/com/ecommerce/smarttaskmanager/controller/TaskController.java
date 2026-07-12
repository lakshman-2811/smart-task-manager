package com.ecommerce.smarttaskmanager.controller;

import com.ecommerce.smarttaskmanager.dto.TaskRequestDto;
import com.ecommerce.smarttaskmanager.dto.TaskResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;
import com.ecommerce.smarttaskmanager.enums.TaskPriority;
import com.ecommerce.smarttaskmanager.enums.TaskStatus;
import com.ecommerce.smarttaskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public Page<TaskResponseDto> getAllTasks(@RequestParam(defaultValue = "0")
                                             int page, @RequestParam(defaultValue = "5") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String direction) {

        return taskService.getAllTasks(page, size, sortBy, direction);
    }

    @PostMapping("/tasks")
    public TaskResponseDto createTask(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        return taskService.createTask(taskRequestDto);
    }

    @GetMapping("/tasks/{id}")
    public TaskResponseDto getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    @PutMapping("/tasks/{id}")
    public TaskResponseDto updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto taskRequestDto) {

        return taskService.updateTask(id, taskRequestDto);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/search")
    public List<TaskResponseDto> searchTasks(@RequestParam String keyword) {

        return taskService.searchTasks(keyword);
    }

    @GetMapping("/tasks/filter/status")
    public List<TaskResponseDto> getTasksByStatus(
            @RequestParam TaskStatus status) {

        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/tasks/filter/priority")
    public List<TaskResponseDto> getTasksByPriority(
            @RequestParam TaskPriority priority) {

        return taskService.getTasksByPriority(priority);
    }

    @GetMapping("/tasks/filter")
    public Page<TaskResponseDto> filterTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return taskService.filterTasks(status, priority, title, page, size, sortBy, direction);
    }
}