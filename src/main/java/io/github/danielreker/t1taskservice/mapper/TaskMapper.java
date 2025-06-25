package io.github.danielreker.t1taskservice.mapper;

import io.github.danielreker.t1taskservice.model.Task;
import io.github.danielreker.t1taskservice.model.dto.CreateTaskRequest;
import io.github.danielreker.t1taskservice.model.dto.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.Duration;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = { Duration.class }
)
public interface TaskMapper {
    @Mapping(target = "duration", expression = "java(Duration.ofMillis(createTaskRequest.durationMs()))")
    Task toEntity(CreateTaskRequest createTaskRequest);

    @Mapping(target = "durationMs", expression = "java(task.duration().toMillis())")
    TaskDto toDto(Task task);
}
