package task.backend.api.unittests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import task.backend.api.CreateTaskRequest;
import task.backend.api.CreateTaskResponse;
import task.backend.api.TaskEntity;
import task.backend.api.TaskStatus;
import task.backend.api.controller.TaskController;
import task.backend.api.mapper.TaskMapper;
import task.backend.api.repository.TaskRepository;;

@WebMvcTest(controllers = TaskController.class,
                excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTests {

        
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TaskRepository repository;

        @MockBean
        private TaskMapper mapper;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void givenNewTaskRequest_thenServiceExecutesSave()
                        throws JsonProcessingException, Exception {

                LocalDate dueDate = LocalDate.of(2020, 12, 12);

                UUID expectedTaskId = UUID.randomUUID();
                
                TaskEntity expectedSavedEntity = TaskEntity.builder().description("description")
                                .dueDate(dueDate).status(TaskStatus.IN_PROGRESS)
                                .title("Submit Form").build();

                CreateTaskRequest request = CreateTaskRequest.builder().description("description")
                                .dueDate(dueDate).status(TaskStatus.IN_PROGRESS)
                                .title("Submit Form").build();

                when(mapper.requestToEntity(request)).thenReturn(expectedSavedEntity);
                TaskEntity expectedPostSaveEntity = TaskEntity.builder().taskId(expectedTaskId).id(123L).build();
                when(repository.save(expectedSavedEntity)).thenReturn(
                                expectedPostSaveEntity);

                assertDoesNotThrow(() -> {
                        String writeValueAsString = objectMapper.writeValueAsString(request);
                        return mockMvc.perform(post("/api/v1/task").accept("application/json")
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .content(writeValueAsString))
                                        .andExpectAll(result -> assertThat(
                                                        result.getResponse().getStatus())
                                                                        .isEqualTo(201),
                                                        result -> objectMapper.readValue(result
                                                                        .getResponse()
                                                                        .getContentAsString(),
                                                                        new TypeReference<CreateTaskResponse>() {}));
                });

        }
}
