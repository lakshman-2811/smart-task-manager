package com.ecommerce.smarttaskmanager.specification;

import com.ecommerce.smarttaskmanager.entity.Task;
import com.ecommerce.smarttaskmanager.enums.TaskPriority;
import com.ecommerce.smarttaskmanager.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) ->
                priority == null ? null : criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                title == null || title.isBlank()
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%");
    }
}