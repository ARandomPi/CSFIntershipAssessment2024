package project.event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class EventManager extends GeneralUser {

    public EventManager() {
    }

    public EventManager(String name) {
        super(name);
    }
}
