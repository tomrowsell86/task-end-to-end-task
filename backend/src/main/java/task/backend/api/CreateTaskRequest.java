package task.backend.api;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreateTaskRequest {
    String title;
    String description;
    TaskStatus status;
    LocalDate dueDate;
}
