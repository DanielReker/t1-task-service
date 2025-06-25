package io.github.danielreker.t1taskservice.model.dto;

public record CreateTaskRequest(
        String description,
        long durationMs
) {
}
