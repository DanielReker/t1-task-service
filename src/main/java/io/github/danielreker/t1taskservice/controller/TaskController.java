package io.github.danielreker.t1taskservice.controller;

import io.github.danielreker.t1taskservice.mapper.TaskMapper;
import io.github.danielreker.t1taskservice.model.dto.CreateTaskRequest;
import io.github.danielreker.t1taskservice.model.dto.TaskDto;
import io.github.danielreker.t1taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private final TaskMapper mapper;


    @GetMapping
    public ResponseEntity<List<TaskDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream()
                .map(mapper::toDto)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody CreateTaskRequest createTaskRequest) {
        return ResponseEntity.ok(mapper.toDto(service.createTask(createTaskRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelById(@PathVariable long id) {
        service.cancelTask(id);
        return ResponseEntity.noContent().build();
    }

}
