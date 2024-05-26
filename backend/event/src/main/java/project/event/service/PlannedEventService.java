package project.event.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.event.dao.EventManagerRepository;
import project.event.dao.PlannedEventRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.EventManager;
import project.event.model.PlannedEvent;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

@Service
public class PlannedEventService {

    @Autowired
    private PlannedEventRepository plannedEventRepository;

    @Autowired
    private EventManagerRepository eventManagerRepository;

    /**
     * Create a planned event using objects
     * @param eventManager the event manager creating the event
     * @param eventName the name of the event
     * @param description the description of the event (optional)
     * @param location the location of the event
     * @param date the date of the event
     * @return the planned event created
     */
    @Transactional
    public PlannedEvent createPlannedEvent(EventManager eventManager, String eventName, String description,
                                           String location, GregorianCalendar date) {
        if (eventManager == null) {
            throw new EventRegistrationAppException("Event manager must be instantiated", HttpStatus.BAD_REQUEST);
        }
        if (eventName == null || eventName.isEmpty()) {
            throw new EventRegistrationAppException("Event name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        // A description is optional, so it won't be checked
        if (location == null || location.isEmpty()) {
            throw new EventRegistrationAppException("Location cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (date == null) {
            throw new EventRegistrationAppException("Date must be instantiated", HttpStatus.BAD_REQUEST);
        }
        if (date.compareTo(new GregorianCalendar(Locale.CANADA_FRENCH)) < 0) {
            throw new EventRegistrationAppException("Date cannot be in the past", HttpStatus.BAD_REQUEST);
        }
        PlannedEvent plannedEvent = new PlannedEvent(eventManager, eventName, description, location, date);
        plannedEventRepository.save(plannedEvent);
        return plannedEvent;
    }

    /**
     * Create a planned event using ids
     * @param eventManagerId the event manager id creating the event
     * @param eventName the name of the event
     * @param description the description of the event (optional)
     * @param location the location of the event
     * @param date the date of the event
     * @return the planned event created
     */
    @Transactional
    public PlannedEvent createPlannedEvent(int eventManagerId, String eventName, String description,
                                           String location, GregorianCalendar date) {
        EventManager eventManager = eventManagerRepository.findEventManagerByGeneralUserId(eventManagerId);
        if (eventManager == null) {
            throw new EventRegistrationAppException("Event manager not found", HttpStatus.NOT_FOUND);
        }
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new EventRegistrationAppException("Event name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        // A description is optional, so it won't be checked
        if (location == null || location.trim().isEmpty()) {
            throw new EventRegistrationAppException("Location cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (date == null) {
            throw new EventRegistrationAppException("Date must be instantiated", HttpStatus.BAD_REQUEST);
        }
        if (date.compareTo(new GregorianCalendar(Locale.CANADA_FRENCH)) < 0) {
            throw new EventRegistrationAppException("Date cannot be in the past", HttpStatus.BAD_REQUEST);
        }
        PlannedEvent plannedEvent = new PlannedEvent(eventManager, eventName, description, location, date);
        plannedEventRepository.save(plannedEvent);
        return plannedEvent;
    }

    /**
     * Update a planned event
     * @param plannedEventId the id of the planned event to update
     * @param eventName the updated name of the event
     * @param description the updated description of the event (optional)
     * @param location the updated location of the event
     * @param date the updated date of the event
     * @return the updated planned event
     */
    @Transactional
    public PlannedEvent updatePlannedEvent(int plannedEventId, String eventName, String description,
                                           String location, GregorianCalendar date) {
        PlannedEvent plannedEvent = plannedEventRepository.findPlannedEventByPlannedEventId(plannedEventId);
        if (plannedEvent == null) {
            throw new EventRegistrationAppException("Planned event not found", HttpStatus.NOT_FOUND);
        }
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new EventRegistrationAppException("Event name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        // A description is optional, so it won't be checked
        if (location == null || location.trim().isEmpty()) {
            throw new EventRegistrationAppException("Location cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (date == null) {
            throw new EventRegistrationAppException("Date must be instantiated", HttpStatus.BAD_REQUEST);
        }
        if (date.compareTo(new GregorianCalendar(Locale.CANADA_FRENCH)) < 0) {
            throw new EventRegistrationAppException("Date cannot be in the past", HttpStatus.BAD_REQUEST);
        }
        plannedEvent.setPlannedEventName(eventName);
        plannedEvent.setDescription(description);
        plannedEvent.setLocation(location);
        plannedEvent.setDate(date);
        plannedEventRepository.save(plannedEvent);
        return plannedEvent;
    }

    /**
     * Delete a planned event
     * @param plannedEventId the id of the planned event to delete
     * @return true if the planned event is deleted
     */
    @Transactional
    public Boolean deletePlannedEvent(int plannedEventId) {
        PlannedEvent plannedEvent = plannedEventRepository.findPlannedEventByPlannedEventId(plannedEventId);
        if (plannedEvent == null) {
            throw new EventRegistrationAppException("Planned event not found", HttpStatus.NOT_FOUND);
        }
        try {
            plannedEventRepository.delete(plannedEvent);
        } catch (Exception e) {
            throw new EventRegistrationAppException("Planned event could not be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    /**
     * Get a planned event by its id
     * @param plannedEventId the id of the planned event to be retrieved
     * @return the planned event
     */
    @Transactional
    public PlannedEvent getPlannedEventById(int plannedEventId) {
        PlannedEvent plannedEvent = plannedEventRepository.findPlannedEventByPlannedEventId(plannedEventId);
        if (plannedEvent == null) {
            throw new EventRegistrationAppException("Planned event not found", HttpStatus.NOT_FOUND);
        }
        return plannedEvent;
    }

    /**
     * Get all planned events
     * @return an ArrayList of all planned events
     */
    @Transactional
    public ArrayList<PlannedEvent> getAllPlannedEvents() {
        return new ArrayList<>(plannedEventRepository.findAll());
    }


}
