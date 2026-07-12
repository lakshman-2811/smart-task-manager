package com.ecommerce.smarttaskmanager.service;

import com.ecommerce.smarttaskmanager.dto.TaskRequestDto;
import com.ecommerce.smarttaskmanager.dto.TaskResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    TaskResponseDto createTask(TaskRequestDto taskRequestDto);

    TaskResponseDto getTaskById(Long id);

    TaskResponseDto updateTask(TaskRequestDto taskRequestDto);

}