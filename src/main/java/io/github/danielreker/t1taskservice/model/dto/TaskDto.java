package io.github.danielreker.t1taskservice.model.dto;

import io.github.danielreker.t1taskservice.model.enums.TaskStatus;

import java.time.Instant;

public record TaskDto(
        Long id,
        String description,
        long durationMs,
        TaskStatus taskStatus,
        Instant createdDate,
        Instant modifiedDate
) {
}
