package com.timolisa.activitytracker.Services;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    void saveTask(TaskDTO taskDTO);
    void updateTask(TaskDTO taskDTO) throws TaskNotFoundException;
    List<TaskDTO> findAllTasks();
    List<TaskDTO> findAllPendingTasks();
    List<TaskDTO> findAllTasksInProgress();
    List<TaskDTO> findAllCompletedTasks();
    Optional<TaskDTO> findTaskById(Long id);
    List<TaskDTO> findTasksByStatus(String status);
}
