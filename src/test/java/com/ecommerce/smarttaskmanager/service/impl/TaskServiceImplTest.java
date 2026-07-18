package com.ecommerce.smarttaskmanager.service.impl;

import com.ecommerce.smarttaskmanager.dto.TaskRequestDto;
import com.ecommerce.smarttaskmanager.dto.TaskResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;
import com.ecommerce.smarttaskmanager.entity.User;
import com.ecommerce.smarttaskmanager.enums.TaskPriority;
import com.ecommerce.smarttaskmanager.enums.TaskStatus;
import com.ecommerce.smarttaskmanager.repository.TaskRepository;
import com.ecommerce.smarttaskmanager.repository.UserRepository;
import com.ecommerce.smarttaskmanager.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskRequestDto requestDto;

    @BeforeEach
    void setUp() {

        task = new Task();

        task.setId(1L);
        task.setTitle("Learn Spring Boot");
        task.setDescription("Complete DTO Layer");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDate.now());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        requestDto = new TaskRequestDto();

        requestDto.setTitle("Learn Spring Boot");
        requestDto.setDescription("Complete DTO Layer");
        requestDto.setStatus(TaskStatus.PENDING);
        requestDto.setPriority(TaskPriority.HIGH);
        requestDto.setDueDate(LocalDate.now());
    }

    @Test
    void shouldCreateTaskSuccessfully() {

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        TaskResponseDto response =
                taskService.createTask(requestDto);

        assertNotNull(response);

        assertEquals(
                "Learn Spring Boot",
                response.getTitle());

        assertEquals(
                TaskStatus.PENDING,
                response.getStatus());

        verify(taskRepository, times(1))
                .save(any(Task.class));
    }

    @Test
    void shouldGetTaskByIdSuccessfully() {

        when(taskRepository.findById(1L))
                .thenReturn(java.util.Optional.of(task));

        TaskResponseDto response =
                taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Learn Spring Boot",
                response.getTitle());

        verify(taskRepository)
                .findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {

        when(taskRepository.findById(1L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                com.ecommerce.smarttaskmanager.exception.TaskNotFoundException.class,
                () -> taskService.getTaskById(1L));

        verify(taskRepository)
                .findById(1L);
    }

    @Test
    void shouldDeleteTaskSuccessfully() {

        when(taskRepository.findById(1L))
                .thenReturn(java.util.Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository)
                .delete(task);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {

        when(taskRepository.findById(1L))
                .thenReturn(java.util.Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        TaskResponseDto response =
                taskService.updateTask(1L, requestDto);

        assertNotNull(response);

        verify(taskRepository)
                .findById(1L);

        verify(taskRepository)
                .save(any(Task.class));
    }

    @Test
    void shouldSearchTasksSuccessfully() {

        when(taskRepository.findByTitleContainingIgnoreCase("Spring"))
                .thenReturn(List.of(task));

        List<TaskResponseDto> response =
                taskService.searchTasks("Spring");

        assertEquals(1, response.size());

        verify(taskRepository)
                .findByTitleContainingIgnoreCase("Spring");
    }

    @Test
    void shouldGetTasksByStatusSuccessfully() {

        when(taskRepository.findByStatus(TaskStatus.PENDING))
                .thenReturn(List.of(task));

        List<TaskResponseDto> response =
                taskService.getTasksByStatus(
                        TaskStatus.PENDING);

        assertEquals(1, response.size());

        verify(taskRepository)
                .findByStatus(TaskStatus.PENDING);
    }

    @Test
    void shouldGetTasksByPrioritySuccessfully() {

        when(taskRepository.findByPriority(TaskPriority.HIGH))
                .thenReturn(List.of(task));

        List<TaskResponseDto> response =
                taskService.getTasksByPriority(
                        TaskPriority.HIGH);

        assertEquals(1, response.size());

        verify(taskRepository)
                .findByPriority(TaskPriority.HIGH);
    }

    @Test
    void shouldAssignTaskSuccessfully() {

        User user = new User();

        user.setId(1L);
        user.setName("Lakshman");

        when(taskRepository.findById(1L))
                .thenReturn(java.util.Optional.of(task));

        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        TaskResponseDto response =
                taskService.assignTask(1L, 1L);

        assertNotNull(response);

        verify(taskRepository)
                .findById(1L);

        verify(userRepository)
                .findById(1L);

        verify(taskRepository)
                .save(any(Task.class));
    }




}