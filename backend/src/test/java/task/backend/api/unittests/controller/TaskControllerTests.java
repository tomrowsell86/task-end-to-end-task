package task.backend.api.unittests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import task.backend.api.controller.TaskController;
import task.backend.api.mapper.TaskMapper;
import task.backend.api.model.CreateTaskRequest;
import task.backend.api.model.CreateTaskResponse;
import task.backend.api.model.TaskEntity;
import task.backend.api.model.TaskStatus;
import task.backend.api.repository.TaskRepository;

@WebMvcTest(controllers = TaskController.class)
// @SecurityTestExecutionListeners
// @ContextConfiguration(classes = SecurityConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTests {
        private MockMvc mockMvc;

        @BeforeEach
        void beforeEach() {
                mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                .apply(springSecurity()).build();
        }

        @MockBean
        private SecurityConfig config;

        @InjectMocks
        private TaskController controller;

        @MockBean
        private TaskRepository repository;

        @MockBean
        private TaskMapper mapper;

        @Autowired
        private ObjectMapper objectMapper;

        @WithMockUser
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
                                                                        new TypeReference<CreateTaskResponse>() {
                                                                        }));
                });

        }
}
