package io.github.danielreker.t1taskservice.service;

import io.github.danielreker.t1taskservice.exception.TaskNotFoundException;
import io.github.danielreker.t1taskservice.mapper.TaskMapper;
import io.github.danielreker.t1taskservice.model.Task;
import io.github.danielreker.t1taskservice.model.dto.CreateTaskRequest;
import io.github.danielreker.t1taskservice.model.enums.TaskStatus;
import io.github.danielreker.t1taskservice.repository.InMemoryTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
    private final InMemoryTaskRepository repository;
    private final TaskMapper mapper;
    private final TaskScheduler taskScheduler;


    public List<Task> getAll() {
        return repository.findAll();
    }

    public Task getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task #%s not found".formatted(id)));
    }

    public Task createTask(CreateTaskRequest request) {
        Task newTask = mapper.toEntity(request).toBuilder()
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .taskStatus(TaskStatus.IN_PROGRESS)
                .build();

        Task createdTask = repository.create(newTask);
        scheduleTaskCompletion(createdTask);
        return createdTask;
    }

    public void scheduleTaskCompletion(Task createdTask) {
        taskScheduler.schedule(() -> {
            Task currentTask = repository.findById(createdTask.id())
                    .orElse(null);

            if (currentTask == null) {
                log.warn("Task #{} was scheduled, but is not found", createdTask.id());
                return;
            }

            if (currentTask.taskStatus() == TaskStatus.IN_PROGRESS) {
                Task doneTask = currentTask.toBuilder()
                        .modifiedDate(Instant.now())
                        .taskStatus(TaskStatus.DONE)
                        .build();

                repository.update(currentTask, doneTask);
            }
        }, createdTask.createdDate().plus(createdTask.duration()));
    }

    public void cancelTask(long id) {
        Task currentTask = repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Tried to delete non-existing task (ID: %s)"
                        .formatted(id)));

        if (currentTask.taskStatus() == TaskStatus.DONE) {
            throw new IllegalStateException("Can't cancel already DONE task (ID: %s)"
                    .formatted(currentTask.id()));
        }

        if (currentTask.taskStatus() == TaskStatus.CANCELED) {
            return;
        }

        Task canceledTask = currentTask.toBuilder()
                .modifiedDate(Instant.now())
                .taskStatus(TaskStatus.CANCELED)
                .build();

        if (!repository.update(currentTask, canceledTask)) {
            throw new IllegalStateException("Concurrent modification of task #%s failed".formatted(currentTask.id()));
        }
    }

}
