package com.ecommerce.smarttaskmanager.repository;

import com.ecommerce.smarttaskmanager.entity.Task;
import com.ecommerce.smarttaskmanager.enums.TaskPriority;
import com.ecommerce.smarttaskmanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findByTitleContainingIgnoreCase(String keyword);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByAssignedUserEmail(String email);

    long countByStatus(TaskStatus status);
}