package project.event.model;

import jakarta.persistence.*;
import org.springframework.http.HttpStatus;
import project.event.exception.EventRegistrationAppException;

import java.util.GregorianCalendar;

@Entity
public class PlannedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int plannedEventId;

    @ManyToOne
    private EventManager eventManager;

    private String eventName;
    private String description;
    private String location;
    private GregorianCalendar date;

    public PlannedEvent() {
    }

    public PlannedEvent(EventManager eventManager, String eventName, String description,
                        String location, GregorianCalendar date) {
        if (!setEventManager(eventManager)) {
            throw new RuntimeException("Invalid event manager");
        }
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    // Getters
    public int getPlannedEventId() {
        return this.plannedEventId;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public String getEventName() {
        return this.eventName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public GregorianCalendar getDate() {
        return this.date;
    }

    // Setters

    // NOTE: This setter is ONLY for testing purposes
    public boolean setPlannedEventId(int plannedEventId) {
        this.plannedEventId = plannedEventId;
        return true;
    }

    public boolean setPlannedEventName(String name) {
        this.eventName = name;
        return true;
    }

    public boolean setEventManager(EventManager eventManager) {
        if (eventManager == null) {
            return false;
        }
        this.eventManager = eventManager;
        return true;
    }

    public boolean setDescription(String description) {
        this.description = description;
        return true;
    }

    public boolean setLocation(String location) {
        this.location = location;
        return true;
    }

    public boolean setDate(GregorianCalendar date) {
        if (date == null) {
            return false;
        }
        this.date = date;
        return true;
    }
}
