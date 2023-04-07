package com.timolisa.activitytracker.controller;

import com.timolisa.activitytracker.DTO.TaskDTO;
import com.timolisa.activitytracker.DTO.UserDTO;
import com.timolisa.activitytracker.services.TaskService;
import com.timolisa.activitytracker.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public UserController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ModelAndView showLoginForm() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("userDTO", new UserDTO());
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                              BindingResult bindingResult, HttpSession httpSession) {
        UserDTO existingUser = userService.loginUser(userDTO);
        ModelAndView mav = new ModelAndView();
        if (bindingResult.hasErrors()) {
            mav.setViewName("redirect:/");
        }
        if (existingUser == null) {
            bindingResult.rejectValue("username", "", "No user exists with this username");
            mav.setViewName("redirect:/?error=true");
        }
        assert existingUser != null;
        List<TaskDTO> tasks = taskService
                .findTasksForUser(existingUser.getId());
        httpSession.setAttribute("tasks", tasks);
        httpSession.setAttribute("user", existingUser);
        mav.setViewName("redirect:/tasks");
        return mav;
    }

    @GetMapping("/register")
    public ModelAndView showRegistrationPage() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("userDTO", new UserDTO());
        return mav;
    }

    @PostMapping("/register/user")
    public ModelAndView registerUser(@Valid @ModelAttribute UserDTO userDTO,
                                     BindingResult bindingResult) {
        UserDTO existingUser =
        ModelAndView mav = new ModelAndView("index");

    }
}
