package project.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import project.event.dao.EventManagerRepository;
import project.event.dao.GeneralUserRepository;
import project.event.dao.PlannedEventRepository;
import project.event.dao.RegistrationRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.EventManager;
import project.event.model.PlannedEvent;
import project.event.model.Registration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private GeneralUserRepository generalUserRepository;

    @Mock
    private EventManagerRepository eventManagerRepository;

    @Mock
    private PlannedEventRepository plannedEventRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private final int REGISTRATION_ID = 1;
    private final int NONEXISTING_REGISTRATION_ID = 2;
    private final int PLANNED_EVENT_ID = 3;
    private final int NONEXISTING_PLANNED_EVENT_ID = 4;
    private final int GENERAL_USER_ID = 5;
    private final int NONEXISTING_GENERAL_USER_ID = 6;

    // Setting up the mock output for the registration repository
    @BeforeEach
    public void setMockOutputRegistration() {
        lenient().when(registrationRepository.findRegistrationByRegistrationId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(REGISTRATION_ID)) {
                Registration registration = new Registration();
                registration.setRegistrationId(REGISTRATION_ID);
                return registration;
            } else {
                return null;
            }
        });
        lenient().when(registrationRepository.findAll()).thenAnswer((invocation) -> {
            ArrayList<Registration> registrations = new ArrayList<>();
            Registration registration = new Registration();
            registration.setRegistrationId(REGISTRATION_ID);
            registrations.add(registration);
            return registrations;
        });
        lenient().when(plannedEventRepository.findPlannedEventByPlannedEventId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(PLANNED_EVENT_ID)) {
                PlannedEvent plannedEvent = new PlannedEvent();
                plannedEvent.setPlannedEventId(PLANNED_EVENT_ID);
                return plannedEvent;
            } else {
                return null;
            }
        });
        lenient().when(generalUserRepository.findGeneralUserByGeneralUserId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(GENERAL_USER_ID)) {
                EventManager eventManager = new EventManager();
                eventManager.setGeneralUserId(GENERAL_USER_ID);
                return eventManager;
            } else {
                return null;
            }
        });
    }

    /**
     * Test for creating a registration using objects
     */
    @Test
    public void testCreateRegistration() {
        assertEquals(0, registrationRepository.count());

        EventManager eventManager = new EventManager("manager");
        PlannedEvent plannedEvent = new PlannedEvent(eventManager, "event",
                "description", "location", new GregorianCalendar());

        Registration registration = null;
        try {
            registration = registrationService.createRegistration(plannedEvent, eventManager);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(registration);
        assertEquals(eventManager.getGeneralUserId(), registration.getGeneralUser().getGeneralUserId());
        assertEquals(plannedEvent.getPlannedEventId(), registration.getPlannedEvent().getPlannedEventId());
    }

    /**
     * Test for creating a registration with a null event manager
     */
    @Test
    public void testCreateRegistrationNullEventManager() {
        assertEquals(0, registrationRepository.count());

        EventManager eventManager = new EventManager("manager");
        PlannedEvent plannedEvent = new PlannedEvent(eventManager, "event",
                "description", "location", new GregorianCalendar());

        Registration registration = null;
        String message = null;
        HttpStatus httpStatus = null;
        try {
            registration = registrationService.createRegistration(plannedEvent, null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            httpStatus = e.getStatus();
        }
        assertNull(registration);
        assertEquals("General user must be instantiated", message);
        assertEquals(HttpStatus.BAD_REQUEST, httpStatus);

    }

    /**
     * Test for creating a registration with a null planned event
     */
    @Test
    public void testCreateRegistrationNullPlannedEvent() {
        assertEquals(0, registrationRepository.count());

        EventManager eventManager = new EventManager("manager");
        PlannedEvent plannedEvent = new PlannedEvent(eventManager, "event",
                "description", "location", new GregorianCalendar());

        Registration registration = null;
        String message = null;
        HttpStatus httpStatus = null;
        try {
            registration = registrationService.createRegistration(null, eventManager);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            httpStatus = e.getStatus();
        }
        assertNull(registration);
        assertEquals("Planned event must be instantiated", message);
        assertEquals(HttpStatus.BAD_REQUEST, httpStatus);

    }

    /**
     * Test for creating a registration using ids
     */
    @Test
    public void testCreateRegistrationUsingIds() {
        assertEquals(0, registrationRepository.count());

        Registration registration = null;
        try {
            registration = registrationService.createRegistration(PLANNED_EVENT_ID, GENERAL_USER_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(registration);
        assertEquals(GENERAL_USER_ID, registration.getGeneralUser().getGeneralUserId());
        assertEquals(PLANNED_EVENT_ID, registration.getPlannedEvent().getPlannedEventId());
    }

    /**
     * Test for creating a registration with a non-existing planned event
     */
    @Test
    public void testCreateRegistrationNonExistingPlannedEvent() {
        assertEquals(0, registrationRepository.count());

        Registration registration = null;
        String message = null;
        HttpStatus httpStatus = null;
        try {
            registration = registrationService.createRegistration(NONEXISTING_PLANNED_EVENT_ID, GENERAL_USER_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            httpStatus = e.getStatus();
        }
        assertNull(registration);
        assertEquals("Planned event not found", message);
        assertEquals(HttpStatus.NOT_FOUND, httpStatus);
    }

    /**
     * Test for creating a registration with a non-existing general user
     */
    @Test
    public void testCreateRegistrationNonExistingGeneralUser() {
        assertEquals(0, registrationRepository.count());

        Registration registration = null;
        String message = null;
        HttpStatus httpStatus = null;
        try {
            registration = registrationService.createRegistration(PLANNED_EVENT_ID, NONEXISTING_GENERAL_USER_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            httpStatus = e.getStatus();
        }
        assertNull(registration);
        assertEquals("General user not found", message);
        assertEquals(HttpStatus.NOT_FOUND, httpStatus);
    }

    /**
     * Test for deleting a registration
     */
    @Test
    public void testDeleteRegistration() {
        assertEquals(0, registrationRepository.count());

        boolean result = false;
        try {
            result = registrationService.deleteRegistration(REGISTRATION_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertTrue(result);
    }

    /**
     * Test for deleting a non-existing registration
     */
    @Test
    public void testDeleteNonExistingRegistration() {
        assertEquals(0, registrationRepository.count());

        boolean result = false;
        String message = null;
        HttpStatus httpStatus = null;
        try {
            result = registrationService.deleteRegistration(NONEXISTING_REGISTRATION_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            httpStatus = e.getStatus();
        }

        assertFalse(result);
        assertEquals("Registration not found", message);
        assertEquals(HttpStatus.NOT_FOUND, httpStatus);
    }

    /**
     * Test for getting a registration using ids
     */
    @Test
    public void testGetRegistrationById() {
        assertEquals(0, registrationRepository.count());

        Registration registration = null;
        try {
            registration = registrationService.getRegistrationById(REGISTRATION_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(registration);
        assertEquals(REGISTRATION_ID, registration.getRegistrationId());
    }

    /**
     * Test for getting a non-existing registration
     */
    @Test
    public void testGetNonExistingRegistration() {
        assertEquals(0, registrationRepository.count());

        Registration registration = null;
        String message = null;
        HttpStatus httpStatus = null;
        try {
            registration = registrationService.getRegistrationById(NONEXISTING_REGISTRATION_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            httpStatus = e.getStatus();
        }

        assertNull(registration);
        assertEquals("Registration not found", message);
        assertEquals(HttpStatus.NOT_FOUND, httpStatus);
    }

    /**
     * Test for getting all registrations
     */
    @Test
    public void testGetAllRegistrations() {
        assertEquals(0, registrationRepository.count());

        ArrayList<Registration> registrations = null;
        try {
            registrations = registrationService.getAllRegistrations();
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(registrations);
        assertEquals(1, registrations.size());
    }

}
