package com.personalizedlearning.controller;

import com.personalizedlearning.dto.UserDto;
import com.personalizedlearning.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final UserService userService;

    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllStudents() {
        return ResponseEntity.ok(userService.getUsersByRole("STUDENT"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDto> updateProfile(@PathVariable Long id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }
}
