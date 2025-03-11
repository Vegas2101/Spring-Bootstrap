package spring.springbootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.springbootstrap.entity.Role;
import spring.springbootstrap.entity.User;
import spring.springbootstrap.service.UserService;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "createUser";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("roles") Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Роли не выбраны");
        }
        userService.saveUser(user, roleIds);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{userId}")
    public String editUserForm(@PathVariable("userId") Long userId, Model model) {
        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin";
    }

    @PostMapping("/update-user")
    public String updateUser(@RequestParam Long userId,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam List<Long> roles) {

        User user = userService.findUserById(userId);
        if (user == null) {
            return "redirect:/admin?error=UserNotFound";
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        Set<Role> roleSet = new HashSet<>(userService.getRolesByIds(roles));
        user.setRoles(roleSet);
        userService.saveUser(user, new HashSet<>(roles));
        return "redirect:/admin";
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin";
    }
    @GetMapping("/user/{id}")
    @ResponseBody
    public Map<String, Object> getUserAndRoles(@PathVariable Long id) {
        User user = userService.findUserById(id);
        List<Role> allRoles = userService.getAllRoles();

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("allRoles", allRoles);

        return response;
    }
}