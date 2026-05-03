package task.backend.api.mapper;

import org.mapstruct.Mapper;

import task.backend.api.model.CreateTaskRequest;
import task.backend.api.model.TaskEntity;

@Mapper
public interface TaskMapper{
   TaskEntity requestToEntity(CreateTaskRequest request);
    
}
