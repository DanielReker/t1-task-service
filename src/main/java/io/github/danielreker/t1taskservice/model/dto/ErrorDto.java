package io.github.danielreker.t1taskservice.model.dto;

public record ErrorDto(
        int status,
        String description
) {
}
