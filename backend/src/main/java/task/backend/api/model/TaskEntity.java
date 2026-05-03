package task.backend.api.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class TaskEntity {
    @Id
    private Long id;
    private UUID taskId;
    private String description;
    private String title;
    private LocalDate dueDate;
    private TaskStatus status;
}
