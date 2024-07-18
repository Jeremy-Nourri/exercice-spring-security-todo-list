package com.example.todolistapi.controller;

import com.example.todolistapi.config.jwt.JwtTokenProvider;
import com.example.todolistapi.dto.BaseDtoResponse;
import com.example.todolistapi.model.Todo;
import com.example.todolistapi.model.User;
import com.example.todolistapi.service.TodoService;
import com.example.todolistapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<List <Todo>> findAll() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        Todo todo = todoService.findById(id);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todo);
    }

    @GetMapping("/admin/{userId}/todos")
    public ResponseEntity<List <Todo>> findAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(todoService.findAllByUserId(userId));
    }

    @PostMapping("/admin/{userId}/todo")
    public ResponseEntity<Todo> save(@PathVariable Long userId, @RequestBody Todo todo) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        todo.setUser(user);
        return ResponseEntity.ok(todoService.save(todo));
    }

    @PutMapping("/admin/todo/{id}")
    public ResponseEntity<BaseDtoResponse> updateById(@PathVariable Long id, @RequestBody Todo todo) {
        String token = request.getHeader("Authorization").substring(7);
        Todo updatedTodo = todoService.updateById(id, todo, token);
        if (updatedTodo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseDtoResponse("You are not allowed to update this todo", null));
        }
        return ResponseEntity.ok(new BaseDtoResponse("Todo updated successfully", updatedTodo));
    }

    @DeleteMapping("/admin/todo/{id}")
    public ResponseEntity<BaseDtoResponse> deleteById(@PathVariable Long id) {
        String token = request.getHeader("Authorization").substring(7);
        if (todoService.deleteById(id, token)) {
            return ResponseEntity.ok(new BaseDtoResponse("Todo deleted successfully", null));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseDtoResponse("You are not allowed to delete this todo", null));

    }

}
