package project.event.dao;

import org.springframework.data.repository.CrudRepository;
import project.event.model.EventManager;

import java.util.List;

public interface EventManagerRepository extends CrudRepository<EventManager, Integer>{
    EventManager findEventManagerByGeneralUserId(int eventManagerId);
    List<EventManager> findAll();
}
