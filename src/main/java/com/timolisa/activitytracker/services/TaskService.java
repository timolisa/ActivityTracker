package com.timolisa.activitytracker.services;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    void saveTask(TaskDTO taskDTO);

    void updateTask(TaskDTO taskDTO) throws TaskNotFoundException;

    List<TaskDTO> findAllTasks();

    Optional<TaskDTO> findTaskById(Long id);

    List<TaskDTO> findTasksForUser(Long userId);

    List<TaskDTO> findTasksByStatus(Status status);

    void deleteTaskById(Long id);

    List<TaskDTO> searchTasks(String query);
}
