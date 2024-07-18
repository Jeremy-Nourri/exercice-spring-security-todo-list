package com.example.todolistapi.service;

import com.example.todolistapi.model.Todo;

import java.util.List;

public interface ITodoService {
    List<Todo> findAll();
    Todo findById(Long id);
    Todo save(Todo todo);

    boolean deleteById(Long id, String token);

    Todo updateById(Long id, Todo todo, String token);
    List<Todo> findAllByUserId(Long userId);
}
