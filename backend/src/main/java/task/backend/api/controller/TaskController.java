package task.backend.api.controller;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import task.backend.api.CreateTaskRequest;
import task.backend.api.CreateTaskResponse;
import task.backend.api.TaskEntity;
import task.backend.api.mapper.TaskMapper;
import task.backend.api.repository.TaskRepository;

@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    @PostMapping("/api/v1/task")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest request) {
        TaskEntity requestToEntity = mapper.requestToEntity(request);
        TaskEntity savedTask = repository.save(requestToEntity);
        return CreateTaskResponse.builder().taskId(savedTask.getTaskId()).build();

    }



}
