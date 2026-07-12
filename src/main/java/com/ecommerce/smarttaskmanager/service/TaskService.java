package com.ecommerce.smarttaskmanager.service;

import com.ecommerce.smarttaskmanager.dto.TaskRequestDto;
import com.ecommerce.smarttaskmanager.dto.TaskResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    Page<TaskResponseDto> getAllTasks(int page, int size, String sortBy, String direction);

    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    TaskResponseDto getTaskById(Long id);

    TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto);

    void deleteTask(Long id);

    List<TaskResponseDto> searchTasks(String keyword);
}