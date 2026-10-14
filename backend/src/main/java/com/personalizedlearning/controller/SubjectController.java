package com.personalizedlearning.controller;

import com.personalizedlearning.dto.SubjectDto;
import com.personalizedlearning.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getActiveSubjects());
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubjectDto>> getAllSubjectsAdmin() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @PostMapping
    public ResponseEntity<SubjectDto> createSubject(@Valid @RequestBody SubjectDto dto,
                                                    @RequestParam(required = false, defaultValue = "1") Long createdBy) {
        SubjectDto created = subjectService.createSubject(dto, createdBy);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long id, @RequestBody SubjectDto dto) {
        return ResponseEntity.ok(subjectService.updateSubject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Subject deleted successfully");
        return ResponseEntity.ok(resp);
    }
}
