package project.event.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.event.dao.EventManagerRepository;
import project.event.dao.GeneralUserRepository;
import project.event.dao.PlannedEventRepository;
import project.event.dao.RegistrationRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.GeneralUser;
import project.event.model.PlannedEvent;
import project.event.model.Registration;

import java.util.ArrayList;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PlannedEventRepository plannedEventRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private EventManagerRepository eventManagerRepository;

    /**
     * Create a registration for a planned event using objects
     * @param plannedEvent the planned event to register for
     * @param generalUser the general user registering
     * @return the registration created
     */
    @Transactional
    public Registration createRegistration(PlannedEvent plannedEvent, GeneralUser generalUser) {
        if (generalUser == null) {
            throw new EventRegistrationAppException("General user must be instantiated", HttpStatus.BAD_REQUEST);
        }
        if (plannedEvent == null) {
            throw new EventRegistrationAppException("Planned event must be instantiated", HttpStatus.BAD_REQUEST);
        }
        Registration registration = new Registration(plannedEvent, generalUser);
        registrationRepository.save(registration);
        return registration;
    }

    /**
     * Create a registration for a planned event using ids
     * @param plannedEventId the planned event id to register for
     * @param generalUserId the general user id registering
     * @return the registration created
     */
    @Transactional
    public Registration createRegistration(int plannedEventId, int generalUserId) {
        PlannedEvent plannedEvent = plannedEventRepository.findPlannedEventByPlannedEventId(plannedEventId);
        if (plannedEvent == null) {
            throw new EventRegistrationAppException("Planned event not found", HttpStatus.NOT_FOUND);
        }
        GeneralUser generalUser = generalUserRepository.findGeneralUserByGeneralUserId(generalUserId);
        if (generalUser == null) {
            // Try to find the user as an event manager
            generalUser = eventManagerRepository.findEventManagerByGeneralUserId(generalUserId);
            // Throw error is still not found
            if (generalUser == null) {
                throw new EventRegistrationAppException("General user not found", HttpStatus.NOT_FOUND);
            }
        }
        Registration registration = new Registration(plannedEvent, generalUser);
        registrationRepository.save(registration);
        return registration;
    }

    /**
     * Get a registration by its id
     * @param registrationId the id of the registration to be deleted
     * @return true if the registration is deleted
     */
    @Transactional
    public Boolean deleteRegistration(int registrationId) {
        Registration registration = registrationRepository.findRegistrationByRegistrationId(registrationId);
        if (registration == null) {
            throw new EventRegistrationAppException("Registration not found", HttpStatus.NOT_FOUND);
        }
        try {
            registrationRepository.delete(registration);
        } catch (Exception e) {
            throw new EventRegistrationAppException("Registration could not be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    /**
     * Get a registration by its id
     * @param registrationId the id of the registration to be retrieved
     * @return the registration
     */
    @Transactional
    public Registration getRegistrationById(int registrationId) {
        Registration registration = registrationRepository.findRegistrationByRegistrationId(registrationId);
        if (registration == null) {
            throw new EventRegistrationAppException("Registration not found", HttpStatus.NOT_FOUND);
        }
        return registration;
    }

    /**
     * Get all registrations
     * @return an ArrayList of all registrations
     */
    @Transactional
    public ArrayList<Registration> getAllRegistrations() {
        return new ArrayList<>(registrationRepository.findAll());
    }
}
