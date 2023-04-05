package com.timolisa.activitytracker.ServicesImpl;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.Repository.TaskRepository;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.utils.TaskMapper;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
    private TaskServiceImpl taskService;
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    @BeforeEach
    public void setup() {
        // https://www.baeldung.com/mockito-annotations
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(taskRepository, taskMapper);
    }

    @Test
    void shouldSaveTask() {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        taskService.saveTask(taskDTO);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldUpdateTask() {
        TaskDTO taskDTO = new TaskDTO();
        Task task = new Task();
        when(taskMapper.toTask(taskDTO)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        taskService.updateTask(taskDTO);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void findTaskById() {
        Task task = new Task();
        task.setId(1L);
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        Optional<Task> optionalTask = Optional.of(task);
        when(taskRepository.findTaskById(1L)).thenReturn(optionalTask);
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);
        Optional<TaskDTO> optionalTaskDTO = taskService.findTaskById(1L);
        long expected = optionalTaskDTO
                .get().getId();
        long actual = optionalTask.get().getId();
        assertEquals(expected, actual);
    }

    @Test
    void findAllTasks() {
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        List<TaskDTO> taskDTOList = new ArrayList<>();
        taskDTOList.add(taskDTO);
        when(taskRepository.findAllTasks()).thenReturn(taskList);
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);
        List<TaskDTO> taskDTOS = taskService.findAllTasks();
        assertEquals(taskDTOS.size(), taskDTOList.size());
    }

    @Test
    void findTasksByStatus() {
        Task task = new Task();
        TaskDTO taskDTO = new TaskDTO();
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        List<TaskDTO> taskDTOList = new ArrayList<>();
        taskDTOList.add(taskDTO);
        when(taskRepository.findTaskByStatus(Status.COMPLETED)).thenReturn(taskList);
        when(taskMapper.toTaskDTO(task)).thenReturn(taskDTO);
        List<TaskDTO> taskDTOS = taskService.findTasksByStatus(Status.COMPLETED);
        assertEquals(taskDTOS.size(), taskDTOList.size());
    }
}