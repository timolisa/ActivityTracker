package com.timolisa.activitytracker.Controller;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Model.Task;
import com.timolisa.activitytracker.Services.TaskService;
import com.timolisa.activitytracker.enums.Status;
import com.timolisa.activitytracker.exceptions.TaskNotFoundException;
import com.timolisa.activitytracker.utils.DateFormatter;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
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
public class TaskController {
    private TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/tasks")
    public ModelAndView viewAllTasks(@RequestParam(value = "status", defaultValue = "all")
                                     String status) {
        List<TaskDTO> tasks;
        if (status.equals("in-progress")) {
            tasks = taskService.findTasksByStatus(Status.IN_PROGRESS);
        } else if (status.equals("completed")) {
            tasks = taskService.findTasksByStatus(Status.COMPLETED);
        } else if (status.equals("pending")) {
            tasks = taskService.findTasksByStatus(Status.PENDING);
        } else {
            tasks = taskService.findAllTasks();
        }
        ModelAndView mav = new ModelAndView("tasks");
        mav.addObject("tasks", tasks);
        mav.addObject("taskDTO", new TaskDTO());
        if (tasks.isEmpty()) {
            mav.addObject("message", "No tasks");
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
            taskService.saveTask(taskDTO.get());
        } else {
            String message = String.format("Task with ID: %s not found", id);
            throw new TaskNotFoundException(message);
        }
        ModelAndView mav = new ModelAndView("redirect:/tasks");
        mav.addObject("message", "Task status updated successfully");
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
        mav.addObject("successMessage", "Product added successfully!");
        mav.setViewName("redirect:/tasks");
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
        ModelAndView mav = new ModelAndView("redirect:/tasks");
        Optional<TaskDTO> taskOptional =
                taskService.findTaskById(taskDTO.getId());
        if (taskOptional.isPresent()) {
            taskService.updateTask(taskDTO);
        } else {
            String message = String.format("Task with ID: %s not found", taskDTO.getId());
            throw new TaskNotFoundException(message);
        }
        return mav;
    }
}
