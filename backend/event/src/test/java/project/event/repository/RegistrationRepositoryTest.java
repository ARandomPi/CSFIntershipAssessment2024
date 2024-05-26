package project.event.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.event.model.*;
import project.event.dao.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private PlannedEventRepository plannedEventRepository;

    @Autowired
    private EventManagerRepository eventManagerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
        plannedEventRepository.deleteAll();
        eventManagerRepository.deleteAll();
        generalUserRepository.deleteAll();
    }

    /**
     * Test the persistence and retrieval of the Registration object.
     */
    @Test
    public void testPersistAndReadRegistration() {

        GeneralUser generalUser = new GeneralUser("name");
        GeneralUser generatedGeneralUser = generalUserRepository.save(generalUser);

        EventManager eventManager = new EventManager("nameM");
        EventManager generatedEventManager = eventManagerRepository.save(eventManager);

        eventManager = eventManagerRepository.findEventManagerByGeneralUserId(generatedEventManager.getGeneralUserId());
        PlannedEvent plannedEvent = new PlannedEvent(eventManager, "event", "description", "location",
                new GregorianCalendar(2021, Calendar.MAY, 1));
        PlannedEvent generatedPlannedEvent = plannedEventRepository.save(plannedEvent);

        Registration registration = new Registration(generatedPlannedEvent, generatedGeneralUser);
        Registration generated = registrationRepository.save(registration);

        Registration returned = registrationRepository.findRegistrationByRegistrationId(generated.getRegistrationId());

        assertNotNull(returned);

        // Check original data
        assertEquals(generalUser.getGeneralUserId(), returned.getGeneralUser().getGeneralUserId());
        assertEquals(plannedEvent.getPlannedEventId(), returned.getPlannedEvent().getPlannedEventId());

        // Check post-save data
        assertEquals(generated.getRegistrationId(), returned.getRegistrationId());
        assertEquals(generated.getGeneralUser().getGeneralUserId(), returned.getGeneralUser().getGeneralUserId());
        assertEquals(generated.getPlannedEvent().getPlannedEventId(), returned.getPlannedEvent().getPlannedEventId());
    }
}
