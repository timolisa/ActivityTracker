package com.timolisa.activitytracker.controller;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.DTO.UserDTO;
import com.timolisa.activitytracker.services.TaskService;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;
import com.timolisa.activitytracker.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@NoArgsConstructor
@Slf4j
public class TaskController {
    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ModelAndView viewAllTasks(@RequestParam(value = "status", defaultValue = "all")
                                     String status,
                                     @RequestParam(value = "successMessage", required = false)
                                     String successMessage,
                                     HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");

        ModelAndView mav = new ModelAndView();
        if (userId == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        List<TaskDTO> tasks = switch (status) {
            case "in-progress" -> taskService.findTasksByUserIdAndStatus(Status.IN_PROGRESS, userId);
            case "completed" -> taskService.findTasksByUserIdAndStatus(Status.COMPLETED, userId);
            case "pending" -> taskService.findTasksByUserIdAndStatus(Status.PENDING, userId);
            default -> taskService.findTasksForUser(userId);
        };

        mav.setViewName("tasks");
        mav.addObject("tasks", tasks);
        mav.addObject("taskDTO", new TaskDTO());
        if (tasks.isEmpty()) {
            mav.addObject("message", "No tasks");
        }
        if (successMessage != null) {
            mav.addObject("successMessage", successMessage);
        }
        return mav;
    }

    @GetMapping("/tasks/{id}")
    public ModelAndView viewTask(@PathVariable Long id, HttpSession httpSession) throws TaskNotFoundException {
        ModelAndView mav = new ModelAndView("task-detail");
        Long userId = (Long) httpSession.getAttribute("userId");
        Optional<TaskDTO> optionalTask = taskService.findTaskForUser(id, userId);

        if (userId == null) {
            return new ModelAndView("index");
        }
        if (optionalTask.isPresent()) {
            TaskDTO task = optionalTask.get();
            mav.addObject("task", task);
        } else {
            String message = String.format("Task with ID: %s not found", id);
            throw new TaskNotFoundException(message);
        }
        return mav;
    }

    @PostMapping("/tasks/updateStatus/{id}")
    public ModelAndView updateTaskStatus(@PathVariable Long id,
                                         @RequestParam("status") String status,
                                         HttpSession httpSession) throws TaskNotFoundException {
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            return new ModelAndView("index");
        }

        Optional<TaskDTO> optionalTaskDTO = taskService.findTaskForUser(id, userId);
        TaskDTO taskDTO = optionalTaskDTO.orElseThrow(() -> {
            String message = String.format("Task with ID: %s not found", id);
            return new TaskNotFoundException(message);
        });

        taskDTO.setStatus(status);
        taskDTO.setCompletedAt(LocalDateTime.now());
        taskService.saveTask(taskDTO, userId);

        log.info("Task with ID {} Status: {}",
                taskDTO.getId(),
                taskDTO.getStatus());

        ModelAndView mav = new ModelAndView("redirect:/tasks?successMessage");
        String successMessage = "Task status updated successfully!";
        mav.addObject("successMessage", successMessage);
        return mav;
    }

    @PostMapping("/tasks/new")
    public ModelAndView saveTask(@Valid @ModelAttribute TaskDTO taskDTO,
                                 BindingResult bindingResult,
                                 HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            return new ModelAndView("index");
        }
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mav.addObject("errorMessage", "invalid fields entered");
            mav.setViewName("redirect:/tasks");
        }
        taskService.saveTask(taskDTO, userId);
        String successMessage = "Task added successfully!";
        mav.addObject("successMessage", successMessage);
        mav.setViewName("redirect:/tasks?successMessage");
        return mav;
    }

    @GetMapping("/tasks/edit/{id}")
    public ModelAndView editTask(@PathVariable Long id,
                                 HttpSession httpSession) throws TaskNotFoundException {
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            return new ModelAndView("index");
        }

        Optional<TaskDTO> taskToEdit = taskService.findTaskById(id);
        ModelAndView mav = new ModelAndView("edit-task");

        TaskDTO taskDTO = taskToEdit.orElseThrow(() -> {
            String message = String.format("Task with ID: %s not found", id);
           return new TaskNotFoundException(message);
        });
        mav.addObject("taskToEdit", taskDTO);

        return mav;
    }

    @PostMapping("/tasks/update")
    public ModelAndView updateTask(@ModelAttribute("taskToEdit") TaskDTO taskDTO,
                                   HttpSession httpSession) throws TaskNotFoundException {
        log.info("Task ID {}", taskDTO.getId());
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            return new ModelAndView("index");
        }

        ModelAndView mav = new ModelAndView("redirect:/tasks?successMessage");
        Optional<TaskDTO> taskOptional =
                taskService.findTaskById(taskDTO.getId());

        if (taskOptional.isPresent()) {
            taskService.saveTask(taskDTO, userId);
        } else {
            String message = String.format("Task with ID: %s not found", taskDTO.getId());
            throw new TaskNotFoundException(message);
        }
        String successMessage = "Task updated successfully!";
        mav.addObject("successMessage", successMessage);
        return mav;
    }

    @GetMapping("/tasks/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Long id,
                                   HttpSession httpSession) throws TaskNotFoundException {
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            return new ModelAndView("index");
        }
        Optional<TaskDTO> taskDTOOptional = taskService.findTaskById(id);

        if (taskDTOOptional.isPresent()) {
            TaskDTO taskDTO = taskDTOOptional.get();
            log.info("User who owns task: {}", taskDTO);
            taskService.deleteTaskById(id);
        } else {
            String message = String.format("Task with ID: %s not found", id);
            throw new TaskNotFoundException(message);
        }
        ModelAndView mav = new ModelAndView("redirect:/tasks?successMessage");
        String successMessage = "Task deleted successfully!";
        mav.addObject("successMessage", successMessage);
        return mav;
    }

    @GetMapping("/tasks/search")
    public ModelAndView searchTasks(@RequestParam(name = "query") String query,
                                    HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            return new ModelAndView("index");
        }

        List<TaskDTO> searchResults = taskService.searchTasks(query, userId);
        ModelAndView mav = new ModelAndView("tasks");
        mav.addObject("tasks", searchResults);
        mav.addObject("taskDTO", new TaskDTO());
        mav.addObject("successMessage", "Search Results:");
        return mav;
    }
}
