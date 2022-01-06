package ru.balkonsky.springbootmvc.conrtoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.balkonsky.springbootmvc.model.Role;
import ru.balkonsky.springbootmvc.model.User;
import ru.balkonsky.springbootmvc.service.RoleService;
import ru.balkonsky.springbootmvc.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/index";
    }


    @GetMapping("/user_{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.showUserById(id));
        return "/show";
    }

    @GetMapping("/new_user")
    public String newUser(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "/new_user";
    }

    @PostMapping()
    public String create (@ModelAttribute("user") User user,
                          @RequestParam(required = false, name = "ROLE_ADMIN") String roleAdmin,
                          @RequestParam(required = false, name = "ROLE_USER") String roleUser) {

        Set<Role> roles = new HashSet<>();

        if (roleAdmin != null) {
            roles.add(roleService.getRoleByRoleName("ROLE_ADMIN"));
        }
        if (roleUser != null) {
            roles.add(roleService.getRoleByRoleName("ROLE_USER"));
        }
        if (roleAdmin == null && roleUser == null) {
            roles.add(roleService.getRoleByRoleName("ROLE_USER"));
        }

        user.setRoles(roles);
        userService.saveUser(user);

        return "redirect:/admin";
    }


    @GetMapping("/user_{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("roles",roleService.getAllRoles());
        model.addAttribute("user", userService.showUserById(id));
        return "/edit";
    }

    @PatchMapping("/user_{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id,
                         @RequestParam(required = false, name = "ROLE_ADMIN") String roleAdmin,
                         @RequestParam(required = false, name = "ROLE_USER") String roleUser) {

        Set<Role> roles = new HashSet<>();

        if (roleAdmin != null) {
            roles.add(roleService.getRoleByRoleName("ROLE_ADMIN"));
        }
        if (roleUser != null) {
            roles.add(roleService.getRoleByRoleName("ROLE_USER"));
        }
        if (roleAdmin == null && roleUser == null) {
            roles.add(roleService.getRoleByRoleName("ROLE_USER"));
        }

        user.setRoles(roles);
        userService.updateUser(user, id);

        return "redirect:/admin";
    }

    @DeleteMapping("/user_{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
