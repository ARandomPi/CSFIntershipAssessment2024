package project.event.model;

import jakarta.persistence.*;

@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int registrationId;

    @OneToOne
    private PlannedEvent plannedEvent;

    @OneToOne
    private GeneralUser generalUser;

    public Registration() {
    }

    public Registration(PlannedEvent plannedEvent, GeneralUser generalUser) {
        if (!setPlannedEvent(plannedEvent)) {
            throw new RuntimeException("Invalid planned event");
        }
        if (!setGeneralUser(generalUser)) {
            throw new RuntimeException("Invalid general user");
        }
    }

    // Getters
    public int getRegistrationId() {
        return this.registrationId;
    }

    public PlannedEvent getPlannedEvent() {
        return this.plannedEvent;
    }

    public GeneralUser getGeneralUser() {
        return this.generalUser;
    }

    // Setters

    // NOTE: This setter is ONLY for testing purposes
    public boolean setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
        return true;
    }

    public boolean setPlannedEvent(PlannedEvent plannedEvent) {
        if (plannedEvent == null) {
            return false;
        }
        this.plannedEvent = plannedEvent;
        return true;
    }

    public boolean setGeneralUser(GeneralUser generalUser) {
        if (generalUser == null) {
            return false;
        }
        this.generalUser = generalUser;
        return true;
    }

}
