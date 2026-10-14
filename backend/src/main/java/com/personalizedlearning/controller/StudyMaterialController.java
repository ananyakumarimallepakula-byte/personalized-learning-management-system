package com.personalizedlearning.controller;

import com.personalizedlearning.dto.StudyMaterialDto;
import com.personalizedlearning.service.StudyMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/materials")
public class StudyMaterialController {

    private final StudyMaterialService studyMaterialService;

    public StudyMaterialController(StudyMaterialService studyMaterialService) {
        this.studyMaterialService = studyMaterialService;
    }

    @GetMapping
    public ResponseEntity<List<StudyMaterialDto>> getAllMaterials() {
        return ResponseEntity.ok(studyMaterialService.getAllMaterials());
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<StudyMaterialDto>> getMaterialsByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(studyMaterialService.getMaterialsByTopic(topicId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyMaterialDto> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(studyMaterialService.getMaterialById(id));
    }

    @PostMapping
    public ResponseEntity<StudyMaterialDto> createMaterial(@Valid @RequestBody StudyMaterialDto dto,
                                                           @RequestParam(required = false, defaultValue = "1") Long createdBy) {
        StudyMaterialDto created = studyMaterialService.createMaterial(dto, createdBy);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyMaterialDto> updateMaterial(@PathVariable Long id, @RequestBody StudyMaterialDto dto) {
        return ResponseEntity.ok(studyMaterialService.updateMaterial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMaterial(@PathVariable Long id) {
        studyMaterialService.deleteMaterial(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Study material deleted successfully");
        return ResponseEntity.ok(resp);
    }
}
