package com.example.java5_petshop.controller;

import com.example.java5_petshop.model.Role;
import com.example.java5_petshop.model.User;
import com.example.java5_petshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Mapping for the login page
    @GetMapping("/login")
    public String login() {
        return "Account/login";
    }

    // Mapping for the registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Bind an empty User object to the form
        return "Account/register"; // Return the registration page view
    }

    // Handling form submission for registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Assign the default role to the user (e.g., "USER")
        Long roleId = 2L; // Assuming 2 is the ID for the "USER" role. Replace with your actual role ID.

        // Save the user with the assigned role
        userService.saveUser(user, roleId);

        // Optionally add a success message
        model.addAttribute("successMessage", "Registration successful!");

        // Redirect to the login page after registration
        return "redirect:/login";
    }

}
