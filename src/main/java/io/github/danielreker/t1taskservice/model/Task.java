package io.github.danielreker.t1taskservice.model;

import io.github.danielreker.t1taskservice.model.enums.TaskStatus;
import lombok.Builder;

import java.time.Duration;
import java.time.Instant;

@Builder(toBuilder = true)
public record Task(
        Long id,
        String description,
        Duration duration,
        TaskStatus taskStatus,
        Instant createdDate,
        Instant modifiedDate
) {
}
