package com.timolisa.activitytracker.services;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    void saveTask(TaskDTO taskDTO, Long userId);

    Optional<TaskDTO> findTaskById(Long id);

    List<TaskDTO> findTasksForUser(Long userId);

    Optional<TaskDTO> findTaskForUser(Long id, Long userId);
    List<TaskDTO> findTasksByUserIdAndStatus(Status status, Long userId);

    void deleteTaskById(Long id);

    List<TaskDTO> searchTasks(String query, Long userId);
}
