package task.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import task.backend.api.model.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    
}
