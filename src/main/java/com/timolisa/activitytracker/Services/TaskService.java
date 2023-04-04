package com.timolisa.activitytracker.Services;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Model.Task;

import java.util.List;

public interface TaskService {
    void saveTask(TaskDTO taskDTO);
    List<TaskDTO> findAllTasks();
    List<TaskDTO> findAllPendingTasks();
    List<TaskDTO> findAllTasksInProgress();
    List<TaskDTO> findAllCompletedTasks();
    TaskDTO findTaskById(Long id);
}
