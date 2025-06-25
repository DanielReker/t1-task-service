package io.github.danielreker.t1taskservice.repository;

import io.github.danielreker.t1taskservice.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskRepository {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);


    public Task create(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Created task must not be null");
        }
        if (task.id() != null) {
            throw new IllegalArgumentException("Can't create task with pre-defined ID (expected null ID instead)");
        }

        Task taskWithId = task.toBuilder()
                .id(idSequence.incrementAndGet())
                .build();

        tasks.put(taskWithId.id(), taskWithId);
        return taskWithId;
    }

    public boolean update(Task oldTask, Task newTask) {
        if (newTask == null || oldTask == null) {
            throw new IllegalArgumentException("New and old tasks must not be null");
        }

        Long id = oldTask.id();
        if (id == null) {
            throw new IllegalArgumentException("There's no task with null id (oldTask.id must not be null)");
        }

        if (!id.equals(newTask.id())) {
            throw new IllegalArgumentException("Can't change task ID (provided: oldTask.id = %s, but newTask.id = %s)"
                    .formatted(id, newTask.id()));
        }

        return tasks.replace(id, oldTask, newTask);
    }

    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }
}
