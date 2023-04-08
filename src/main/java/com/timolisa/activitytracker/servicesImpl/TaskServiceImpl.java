package com.timolisa.activitytracker.servicesImpl;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.entity.Task;
import com.timolisa.activitytracker.repository.TaskRepository;
import com.timolisa.activitytracker.repository.UserRepository;
import com.timolisa.activitytracker.services.TaskService;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.utils.TaskMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public void saveTask(TaskDTO taskDTO, Long userId) {
        Task task = taskMapper.toTask(taskDTO);
        task.setUser(userRepository.findById(userId).orElse(null));
        log.info("Task in Service Impl, checking the user: {}", task.getUser());
        taskRepository.save(task);
    }

    @Override
    public Optional<TaskDTO> findTaskById(Long id) {
        Optional<Task> taskOptional = taskRepository.findTaskById(id);
        return taskOptional.map(taskMapper::toTaskDTO);
    }

    // USER PART: fix.
    @Override
    public List<TaskDTO> findTasksForUser(Long userId) {
        return taskRepository.findTasksByUser(userId).stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public Optional<TaskDTO> findTaskForUser(Long id, Long userId) {
        Optional<Task> optionalTask = taskRepository.findTaskForUser(id, userId);
        return optionalTask.map(taskMapper::toTaskDTO);
    }

    @Override
    public List<TaskDTO> findTasksByUserIdAndStatus(Status status, Long userId) {
        return taskRepository.findTasksByUserIdAndStatus(userId, status)
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> searchTasks(String query, Long userId) {
        return taskRepository.search(userId, query)
                .stream()
                .map(taskMapper::toTaskDTO)
                .toList();
    }
}
