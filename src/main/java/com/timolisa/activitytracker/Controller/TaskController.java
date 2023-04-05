package com.timolisa.activitytracker.Controller;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Services.TaskService;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;
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
                                     String successMessage) {
        List<TaskDTO> tasks = switch (status) {
            case "in-progress" -> taskService.findTasksByStatus(Status.IN_PROGRESS);
            case "completed" -> taskService.findTasksByStatus(Status.COMPLETED);
            case "pending" -> taskService.findTasksByStatus(Status.PENDING);
            default -> taskService.findAllTasks();
        };
        ModelAndView mav = new ModelAndView("tasks");
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
    public ModelAndView viewTask(@PathVariable Long id) throws TaskNotFoundException {
        ModelAndView mav = new ModelAndView("task-detail");
        Optional<TaskDTO> optionalTask = taskService.findTaskById(id);
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
                                         @RequestParam("status") String status) throws TaskNotFoundException {
        Optional<TaskDTO> taskDTO = taskService.findTaskById(id);
        if (taskDTO.isPresent()) {
            taskDTO.get().setStatus(String.valueOf(status));
            taskDTO.get().setCompletedAt(LocalDateTime.now());
            taskService.saveTask(taskDTO.get());
            log.info("Task with ID {} Status: {}",
                    taskDTO.get().getId(),
                    taskDTO.get().getStatus());
        } else {
            String message = String.format("Task with ID: %s not found", id);
            throw new TaskNotFoundException(message);
        }
        ModelAndView mav = new ModelAndView("redirect:/tasks?successMessage");
        String successMessage = "Task status updated successfully!";
        mav.addObject("successMessage", successMessage);
        return mav;
    }

    @PostMapping("/tasks/new")
    public ModelAndView saveTask(@Valid @ModelAttribute TaskDTO taskDTO,
                                 BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mav.addObject("errorMessage", "invalid fields entered");
            mav.setViewName("redirect:/tasks");
        }
        taskService.saveTask(taskDTO);
        String successMessage = "Task added successfully!";
        mav.addObject("successMessage", successMessage);
        mav.setViewName("redirect:/tasks?successMessage");
        return mav;
    }

    @GetMapping("/tasks/edit/{id}")
    public ModelAndView editTask(@PathVariable Long id) throws TaskNotFoundException {
        Optional<TaskDTO> taskToEdit = taskService.findTaskById(id);
        ModelAndView mav = new ModelAndView("edit-task");
        if (taskToEdit.isPresent()) {
            mav.addObject("taskToEdit", taskToEdit.get());
        } else {
            String message = String.format("Task with ID: %s not found", id);
            throw new TaskNotFoundException(message);
        }
        return mav;
    }

    @PostMapping("/tasks/update")
    public ModelAndView updateTask(@ModelAttribute("taskToEdit") TaskDTO taskDTO) throws TaskNotFoundException {
        log.info("Task ID {}", taskDTO.getId());
        ModelAndView mav = new ModelAndView("redirect:/tasks?successMessage");
        Optional<TaskDTO> taskOptional =
                taskService.findTaskById(taskDTO.getId());
        if (taskOptional.isPresent()) {
            taskService.updateTask(taskDTO);
        } else {
            String message = String.format("Task with ID: %s not found", taskDTO.getId());
            throw new TaskNotFoundException(message);
        }
        String successMessage = "Task updated successfully!";
        mav.addObject("successMessage", successMessage);
        return mav;
    }

    @GetMapping("/tasks/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        ModelAndView mav = new ModelAndView("redirect:/tasks?successMessage");
        String successMessage = "Task deleted successfully!";
        mav.addObject("successMessage", successMessage);
        return mav;
    }

    @GetMapping("/tasks/search")
    public ModelAndView searchTasks(@RequestParam(name = "query") String query) {
        List<TaskDTO> searchResults = taskService.searchTasks(query);
        ModelAndView mav = new ModelAndView("tasks");
        mav.addObject("tasks", searchResults);
        mav.addObject("taskDTO", new TaskDTO());
        mav.addObject("successMessage", "Search Results:");
        return mav;
    }
}
