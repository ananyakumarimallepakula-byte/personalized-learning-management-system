package com.personalizedlearning.controller;

import com.personalizedlearning.dto.UserDto;
import com.personalizedlearning.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final UserService userService;

    public TeacherController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        return ResponseEntity.ok(userService.getUsersByRole("TEACHER"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
