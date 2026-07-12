package com.ecommerce.smarttaskmanager.mapper;

import com.ecommerce.smarttaskmanager.dto.TaskRequestDto;
import com.ecommerce.smarttaskmanager.dto.TaskResponseDto;
import com.ecommerce.smarttaskmanager.entity.Task;

public interface TaskMapper {

    Task requestToEntity(TaskRequestDto dto);

    TaskResponseDto entityToResponse(Task task);

}
