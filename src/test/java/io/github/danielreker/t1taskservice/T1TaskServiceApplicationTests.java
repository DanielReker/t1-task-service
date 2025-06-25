package io.github.danielreker.t1taskservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.danielreker.t1taskservice.model.dto.CreateTaskRequest;
import io.github.danielreker.t1taskservice.model.dto.TaskDto;
import io.github.danielreker.t1taskservice.model.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class T1TaskServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void contextLoads() {
    }

    @Test
    void whenTryToCancelDoneTask_then400BadRequest() throws Exception {
        CreateTaskRequest createTaskRequest = new CreateTaskRequest("Test task", 100L);

        MvcResult createTaskResult = mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(createTaskRequest.description())))
                .andExpect(jsonPath("$.durationMs", is(createTaskRequest.durationMs().intValue())))
                .andExpect(jsonPath("$.taskStatus", is(TaskStatus.IN_PROGRESS.toString())))
                .andReturn();

        TaskDto createdTask = objectMapper.readValue(
                createTaskResult.getResponse().getContentAsString(),
                TaskDto.class
        );
        long taskId = createdTask.id();

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) taskId)))
                .andExpect(jsonPath("$.taskStatus", is(TaskStatus.IN_PROGRESS.toString())));

        await().atMost(createdTask.durationMs() * 2, TimeUnit.MILLISECONDS)
                .pollInterval(createdTask.durationMs() / 10, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> mockMvc.perform(get("/api/tasks/" + taskId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.taskStatus", is(TaskStatus.DONE.toString()))));

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) taskId)))
                .andExpect(jsonPath("$.taskStatus", is(TaskStatus.DONE.toString())));

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.description").exists());

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) taskId)))
                .andExpect(jsonPath("$.taskStatus", is(TaskStatus.DONE.toString())));
    }

}
