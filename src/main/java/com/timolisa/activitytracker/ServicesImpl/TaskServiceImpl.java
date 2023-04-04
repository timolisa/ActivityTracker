package com.timolisa.activitytracker.ServicesImpl;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.Repository.TaskRepository;
import com.timolisa.activitytracker.Services.TaskService;
import com.timolisa.activitytracker.utils.TaskMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }
    @Override
    public void saveTask(TaskDTO taskDTO) {
        Task task = taskMapper.toTask(taskDTO);
        taskRepository.save(task);
    }
    @Override
    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAllTasks()
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> findAllPendingTasks() {
        return taskRepository.findAllPendingTasks()
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> findAllTasksInProgress() {
        return taskRepository.findAllTasksInProgress()
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> findAllCompletedTasks() {
        return taskRepository.findAllCompletedTasks()
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public TaskDTO findTaskById(Long id) {
        return taskMapper
                .toTaskDTO(taskRepository.findTaskById(id));
    }
}
