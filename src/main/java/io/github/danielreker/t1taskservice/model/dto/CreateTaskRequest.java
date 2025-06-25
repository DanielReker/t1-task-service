package io.github.danielreker.t1taskservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotBlank(message = "Task description cannot be null or blank.")
        String description,

        @NotNull(message = "Task duration cannot be null.")
        @Min(value = 1, message = "Task duration must be at least 1 millisecond.")
        Long durationMs
) {
}
