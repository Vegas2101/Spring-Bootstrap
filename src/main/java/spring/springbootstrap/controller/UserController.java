package spring.springbootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.springbootstrap.entity.Role;
import spring.springbootstrap.entity.User;
import spring.springbootstrap.service.UserService;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userPage(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());
        model.addAttribute("id", user.getId());
        model.addAttribute("email", email);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("age", user.getAge());
        model.addAttribute("roles", roles);
        return "user";
    }
}
