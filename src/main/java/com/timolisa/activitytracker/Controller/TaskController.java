package com.timolisa.activitytracker.Controller;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.Services.TaskService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@NoArgsConstructor
public class TaskController {
    private TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/tasks")
    public ModelAndView viewAllTasks() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("tasks", taskService.findAllTasks());
        return mav;
    }
    @GetMapping("/tasks/{id}")
    public ModelAndView viewTask(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("task-detail");
        mav.addObject("task", taskService.findTaskById(id));
        return mav;
    }
    @GetMapping("/tasks/pending")
    public ModelAndView viewAllPendingTasks() {
        ModelAndView mav = new ModelAndView("tasks");
        mav.addObject("tasks", taskService.findAllPendingTasks());
        return mav;
    }

    @GetMapping("/tasks/in-progress")
    public ModelAndView viewAllTasksInProgress() {
        ModelAndView mav = new ModelAndView("tasks");
        mav.addObject("tasks", taskService.findAllTasksInProgress());
        return mav;
    }

    @GetMapping("/tasks/completed")
    public ModelAndView viewAllCompletedTasks() {
        ModelAndView mav = new ModelAndView("tasks");
        mav.addObject("tasks", taskService.findAllCompletedTasks());
        return mav;
    }
    @PostMapping("/tasks/save")
    public ModelAndView saveTask(@Valid @ModelAttribute TaskDTO taskDTO,
                                 BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mav.setViewName("");
        }
        taskService.saveTask(taskDTO);
        mav.setViewName("redirect:/tasks");
        return mav;
    }
}
