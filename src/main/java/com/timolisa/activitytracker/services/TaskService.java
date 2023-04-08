package com.timolisa.activitytracker.services;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    void saveTaskWithUserId(TaskDTO taskDTO, Long userId);

    void updateTask(TaskDTO taskDTO, Long userId) throws TaskNotFoundException;

    List<TaskDTO> findAllTasks();

    Optional<TaskDTO> findTaskById(Long id);

    List<TaskDTO> findTasksForUser(Long userId);

    Optional<TaskDTO> findTaskForUser(Long id, Long userId);
    List<TaskDTO> findTasksByUserIdAndStatus(Status status, Long userId);

    List<TaskDTO> findTasksByStatus(Status status);

    void deleteTaskById(Long id);

    List<TaskDTO> searchTasks(String query, Long userId);
}
