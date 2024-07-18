package com.example.todolistapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BaseDtoResponse {
    private Object message;
    private Object data;

    public BaseDtoResponse(Object message, Object data) {
        this.message = message;
        this.data = data;
    }

    public BaseDtoResponse(Object message) {
        this.message = message;
    }

}
