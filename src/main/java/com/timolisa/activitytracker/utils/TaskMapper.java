package com.timolisa.activitytracker.utils;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.enums.Status;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDTO toTaskDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(java.lang.String.valueOf(task.getStatus()))
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }

    public Task toTask(TaskDTO taskDTO) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(Status.valueOf(taskDTO.getStatus()))
                .createdAt(taskDTO.getCreatedAt())
                .updatedAt(taskDTO.getUpdatedAt())
                .completedAt(taskDTO.getCompletedAt())
                .build();
    }
}






