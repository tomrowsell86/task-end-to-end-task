package task.backend.api.mapper;

import org.mapstruct.Mapper;

import io.micrometer.common.lang.NonNull;
import task.backend.api.CreateTaskRequest;
import task.backend.api.TaskEntity;

@Mapper
public interface TaskMapper{
   TaskEntity requestToEntity(CreateTaskRequest request);
    
}
