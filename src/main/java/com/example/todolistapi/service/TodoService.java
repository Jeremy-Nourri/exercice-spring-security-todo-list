package com.example.todolistapi.service;

import com.example.todolistapi.model.Todo;
import com.example.todolistapi.repository.TodoRepository;
import com.example.todolistapi.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService implements ITodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo findById(Long id) {
        return todoRepository.findById(id).orElse(null);
    }

    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public boolean deleteById(Long id, String token) {
        Long userIdFromToken = jwtTokenProvider.getUserIdFromToken(token);
        Todo todo = findById(id);
        if (todo == null) {
            return false;
        }
        if (todo.getUser().getId() != userIdFromToken) {
            return false;
        }
        todoRepository.deleteById(id);
        return true;
    }

    @Override
    public Todo updateById(Long id, Todo todo, String Token) {
        Long userIdFromToken = jwtTokenProvider.getUserIdFromToken(Token);
        Todo todoFromDb = findById(id);
        if (todoFromDb == null) {
            return null;
        }
        if (todoFromDb.getUser().getId() != userIdFromToken) {
            return null;
        }
        todoFromDb.setTitle(todo.getTitle());
        todoFromDb.setDescription(todo.getDescription());
        todoFromDb.setDone(todo.isDone());
        return todoRepository.save(todoFromDb);
    }

    @Override
    public List<Todo> findAllByUserId(Long userId) {
        return todoRepository.findAllByUserId(userId);
    }
}
