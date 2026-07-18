package com.ecommerce.smarttaskmanager.controller;

import com.ecommerce.smarttaskmanager.dto.DashboardResponseDto;
import com.ecommerce.smarttaskmanager.dto.UserResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;
import com.ecommerce.smarttaskmanager.entity.User;
import com.ecommerce.smarttaskmanager.enums.TaskStatus;
import com.ecommerce.smarttaskmanager.repository.TaskRepository;
import com.ecommerce.smarttaskmanager.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public AdminController(UserRepository userRepository,
                           TaskRepository taskRepository) {

        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/dashboard")
    public DashboardResponseDto dashboard() {

        DashboardResponseDto dto =
                new DashboardResponseDto();

        dto.setTotalUsers(
                userRepository.count());

        dto.setTotalTasks(
                taskRepository.count());

        dto.setCompletedTasks(
                taskRepository.countByStatus(
                        TaskStatus.COMPLETED));

        dto.setPendingTasks(
                taskRepository.countByStatus(
                        TaskStatus.PENDING));

        return dto;
    }

    @GetMapping("/users")
    public List<UserResponseDto> users() {

        return userRepository.findAll()
                .stream()
                .map(user -> {

                    UserResponseDto dto =
                            new UserResponseDto();

                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole());

                    return dto;
                })
                .toList();
    }

    @GetMapping("/tasks")
    public List<Task> tasks() {

        return taskRepository.findAll();
    }
}