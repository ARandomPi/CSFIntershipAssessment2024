package project.event.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GeneralUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int generalUserId;

    private String name;

    public GeneralUser() {
    }

    public GeneralUser(String name) {
        this.name = name;
    }

    // Getters
    public int getGeneralUserId() {
        return this.generalUserId;
    }

    public String getName() {
        return this.name;
    }

    // Setters

    // NOTE: This setter is ONLY for testing purposes
    public boolean setGeneralUserId(int generalUserId) {
        this.generalUserId = generalUserId;
        return true;
    }

    public boolean setName(String name) {
        if (name == null) {
            return false;
        }
        this.name = name;
        return true;
    }

}
