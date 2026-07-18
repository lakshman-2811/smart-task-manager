package com.ecommerce.smarttaskmanager.service.impl;

import com.ecommerce.smarttaskmanager.dto.TaskRequestDto;
import com.ecommerce.smarttaskmanager.dto.TaskResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;
import com.ecommerce.smarttaskmanager.entity.User;
import com.ecommerce.smarttaskmanager.enums.TaskPriority;
import com.ecommerce.smarttaskmanager.enums.TaskStatus;
import com.ecommerce.smarttaskmanager.exception.TaskNotFoundException;
import com.ecommerce.smarttaskmanager.mapper.TaskMapper;
import com.ecommerce.smarttaskmanager.repository.TaskRepository;
import com.ecommerce.smarttaskmanager.repository.UserRepository;
import com.ecommerce.smarttaskmanager.service.TaskService;
import com.ecommerce.smarttaskmanager.specification.TaskSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<TaskResponseDto> getAllTasks(int page,
                                             int size,
                                             String sortBy,
                                             String direction)  {

        log.info("Fetching all tasks. Page={}, Size={}, SortBy={}, Direction={}",
                page, size, sortBy, direction);
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> taskPage = taskRepository.findAll(pageable);
        log.info("Successfully fetched {} tasks", taskPage.getNumberOfElements());
        return taskPage.map(this::mapToResponse);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {

        log.info("Creating task with title: {}", taskRequestDto.getTitle());
        Task task = new Task();
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        return mapToResponse(savedTask);
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {

        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", id);
                    return new TaskNotFoundException("Task with id " + id + " not found");
                });
        log.info("Task fetched successfully with id: {}", id);
        return mapToResponse(task);
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto) {

        log.info("Updating task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found while updating. Id: {}", id);
                    return new TaskNotFoundException("Task with id " + id + " not found");
                });
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", updatedTask.getId());
        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {

        log.info("Deleting task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found while deleting. Id: {}", id);
                    return new TaskNotFoundException("Task with id " + id + " not found");
                });
        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", id);
    }

    @Override
    public List<TaskResponseDto> searchTasks(String keyword) {

        log.info("Searching tasks with keyword: {}", keyword);
        List<Task> tasks =
                taskRepository.findByTitleContainingIgnoreCase(keyword);
        log.info("Found {} task(s) for keyword '{}'", tasks.size(), keyword);
        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getTasksByStatus(TaskStatus status) {

        log.info("Fetching tasks with status: {}", status);
        List<Task> tasks = taskRepository.findByStatus(status);
        log.info("Found {} task(s) with status {}", tasks.size(), status);
        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getTasksByPriority(TaskPriority priority) {

        log.info("Fetching tasks with priority: {}", priority);
        List<Task> tasks = taskRepository.findByPriority(priority);
        log.info("Found {} task(s) with priority {}", tasks.size(), priority);
        return tasks.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<TaskResponseDto> filterTasks(TaskStatus status,
                                             TaskPriority priority,
                                             String title,
                                             int page,
                                             int size,
                                             String sortBy,
                                             String direction) {

        log.info("Filtering tasks. Status={}, Priority={}, Title={}, Page={}, Size={}",
                status, priority, title, page, size);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Task> specification = Specification
                .where(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.hasPriority(priority))
                .and(TaskSpecification.titleContains(title));

        Page<TaskResponseDto> response = taskRepository.findAll(specification, pageable)
                .map(this::mapToResponse);
        log.info("Filter returned {} task(s)", response.getNumberOfElements());
        return response;
    }

    private TaskResponseDto mapToResponse(Task task) {

        TaskResponseDto response = new TaskResponseDto();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        if (task.getAssignedUser() != null) {
            response.setAssignedUserId(
                    task.getAssignedUser().getId());
            response.setAssignedUserName(
                    task.getAssignedUser().getName());
        }
        return response;
    }

    @Override
    public TaskResponseDto assignTask(Long taskId,
                                      Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        task.setAssignedUser(user);

        Task updatedTask =
                taskRepository.save(task);

        return mapToResponse(updatedTask);
    }
}