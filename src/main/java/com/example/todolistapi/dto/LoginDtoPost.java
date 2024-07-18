package com.example.todolistapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDtoPost {
    private String email;
    private String password;
}
