package com.example.todolistapi.controller;

import com.example.todolistapi.dto.BaseDtoResponse;
import com.example.todolistapi.dto.LoginDtoPost;
import com.example.todolistapi.dto.RegisterDtoPost;
import com.example.todolistapi.model.User;
import com.example.todolistapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseDtoResponse register(@RequestBody RegisterDtoPost registerDtoPost) {
        if (userService.checkIfUserExists(registerDtoPost.getEmail())) {
            return new BaseDtoResponse("User already exists", null);
        }
        User user = new User();
        user.setEmail(registerDtoPost.getEmail());
        user.setPassword(registerDtoPost.getPassword());
        user.setName(registerDtoPost.getName());
        user.setRole(registerDtoPost.getRole());
        userService.save(registerDtoPost);
        return new BaseDtoResponse("User registered successfully", null);
    }

    @PostMapping("/login")
    public BaseDtoResponse login(@RequestBody LoginDtoPost loginDtoPost) {
        if (userService.checkIfUserExists(loginDtoPost.getEmail())) {
            if (userService.checkIfPasswordMatches(loginDtoPost.getEmail(), loginDtoPost.getPassword())) {

                Map<String, Object> data = new HashMap<>();

                data.put("token", userService.generateToken(loginDtoPost.getEmail(), loginDtoPost.getPassword()));

                return new BaseDtoResponse("Your are logged", data);
            } else {
                return new BaseDtoResponse("Error in email or password", null);
            }

        } else {
            return new BaseDtoResponse("User not found", null);
        }

    }
}
