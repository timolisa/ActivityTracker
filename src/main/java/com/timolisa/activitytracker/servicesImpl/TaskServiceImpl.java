package com.timolisa.activitytracker.servicesImpl;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.entity.Task;
import com.timolisa.activitytracker.repository.TaskRepository;
import com.timolisa.activitytracker.services.TaskService;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.utils.TaskMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void updateTask(TaskDTO taskDTO) {
        Task task = taskMapper.toTask(taskDTO);
        taskRepository.save(task);
    }

    @Override
    public Optional<TaskDTO> findTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findTaskById(id);
        return taskOptional.map(taskMapper::toTaskDTO);
    }

    @Override
    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAllTasks()
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> findTasksForUser(Long userId) {
        return taskRepository.findTasksByUserId(userId).stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> findTasksByStatus(Status status) {
        return taskRepository
                .findTaskByStatus(status)
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> searchTasks(String query) {
        return taskRepository.search(query)
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }
}
