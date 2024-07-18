package com.example.todolistapi.dto;

import com.example.todolistapi.model.Role;
import lombok.Data;

@Data
public class RegisterDtoPost {
    private String email;
    private String password;
    private String name;
    private Role role;
}
