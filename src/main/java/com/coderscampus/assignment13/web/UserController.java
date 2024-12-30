package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getCreateUser(ModelMap model) {
        model.put("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postCreateUser(User user) {
        Address newAddress = new Address();
        newAddress.setUser(user);
        user.setAddress(newAddress);
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getAllUsers(ModelMap model) {
        Set<User> users = userService.findAllUsersWithAccountsAndAddresses();
        model.put("users", users);
        if (users.size() == 1) {
            User singleUser = users.iterator().next();
            model.put("user", singleUser);
            model.put("address", singleUser.getAddress());
        }
        return "users";
    }

    @GetMapping("/users/{userId}")
    public String getOneUser(ModelMap model, @PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            model.put("error", "User not found");
            return "error";
        }
        model.put("users", Arrays.asList(user));
        model.put("user", user);
        model.put("address", user.getAddress());
        return "users";
    }

    @PostMapping("/users/{userId}")
    public String postOneUser(@PathVariable Long userId, User user, Address address, ModelMap model) {
        User originalUser = userService.findById(userId);
        if (originalUser == null) {
            model.put("error", "User not found");
            return "error";
        }

        originalUser.setUsername(user.getUsername());
        originalUser.setName(user.getName());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            originalUser.setPassword(user.getPassword());
        }

        Address originalAddress = originalUser.getAddress();
        if (originalAddress == null) {
            originalAddress = new Address();
            originalAddress.setUser(originalUser);
            originalUser.setAddress(originalAddress);
        }

        originalAddress.setAddressLine1(address.getAddressLine1());
        originalAddress.setAddressLine2(address.getAddressLine2());
        originalAddress.setCity(address.getCity());
        originalAddress.setRegion(address.getRegion());
        originalAddress.setCountry(address.getCountry());
        originalAddress.setZipCode(address.getZipCode());

        userService.saveUser(originalUser);

        return "redirect:/users";
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteOneUser(@PathVariable Long userId) {
        userService.delete(userId);
        return "redirect:/users";
    }
}
