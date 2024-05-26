package project.event.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.event.dao.EventManagerRepository;
import project.event.dao.GeneralUserRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.EventManager;
import project.event.model.GeneralUser;

import java.util.ArrayList;

@Service
public class EventManagerService {

    @Autowired
    private EventManagerRepository eventManagerRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    /**
     * Create an event manager
     * @param name the name of the event manager
     * @return the event manager created
     */
    @Transactional
    public EventManager createEventManager(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EventRegistrationAppException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        // Check if there is a duplicate name
        ArrayList<GeneralUser> generalUsers = new ArrayList<>(generalUserRepository.findAll());
        generalUsers.addAll(eventManagerRepository.findAll());
        for(GeneralUser generalUser : generalUsers) {
            if (generalUser.getName().equals(name)) {
                throw new EventRegistrationAppException("Name already exists", HttpStatus.BAD_REQUEST);
            }
        }
        EventManager eventManager = new EventManager(name);
        eventManagerRepository.save(eventManager);
        return eventManager;
    }

    /**
     * Update an event manager
     * @param eventManagerId the id of the event manager
     * @param name the new name of the event manager
     * @return the event manager updated
     */
    @Transactional
    public EventManager updateEventManager(int eventManagerId, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EventRegistrationAppException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        EventManager eventManager = eventManagerRepository.findEventManagerByGeneralUserId(eventManagerId);
        if (eventManager == null) {
            throw new EventRegistrationAppException("Event manager not found", HttpStatus.BAD_REQUEST);
        }
        // Check if there is a duplicate name
        ArrayList<GeneralUser> generalUsers = new ArrayList<>(generalUserRepository.findAll());
        generalUsers.addAll(eventManagerRepository.findAll());
        for(GeneralUser generalUser : generalUsers) {
            if (generalUser.getName().equals(name)) {
                throw new EventRegistrationAppException("Name already exists", HttpStatus.BAD_REQUEST);
            }
        }
        eventManager.setName(name);
        eventManagerRepository.save(eventManager);
        return eventManager;
    }

    /**
     * Delete an event manager
     * @param eventManagerId the id of the event manager
     * @return true if the event manager was deleted
     */
    @Transactional
    public boolean deleteEventManager(int eventManagerId) {
        EventManager eventManager = eventManagerRepository.findEventManagerByGeneralUserId(eventManagerId);
        if (eventManager == null) {
            throw new EventRegistrationAppException("Event manager not found", HttpStatus.BAD_REQUEST);
        }
        try {
            eventManagerRepository.delete(eventManager);
        } catch (Exception e) {
            throw new EventRegistrationAppException("Event manager cannot be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    /**
     * Get an event manager by its id
     * @param eventManagerId the id of the event manager
     * @return the event manager
     */
    @Transactional
    public EventManager getEventManagerById(int eventManagerId) {
        EventManager eventManager = eventManagerRepository.findEventManagerByGeneralUserId(eventManagerId);
        if (eventManager == null) {
            throw new EventRegistrationAppException("Event manager not found", HttpStatus.NOT_FOUND);
        }
        return eventManager;
    }

    /**
     * Get all event managers
     * @return an ArrayList of all event managers
     */
    @Transactional
    public ArrayList<EventManager> getAllEventManagers() {
        return new ArrayList<>(eventManagerRepository.findAll());
    }
}
