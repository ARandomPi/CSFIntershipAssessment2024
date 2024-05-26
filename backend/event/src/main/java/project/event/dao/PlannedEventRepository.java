package project.event.dao;

import org.springframework.data.repository.CrudRepository;
import project.event.model.PlannedEvent;

import java.util.List;

public interface PlannedEventRepository extends CrudRepository<PlannedEvent, Integer>{
    public PlannedEvent findPlannedEventByPlannedEventId(int plannedEventId);
    public List<PlannedEvent> findAll();
}
