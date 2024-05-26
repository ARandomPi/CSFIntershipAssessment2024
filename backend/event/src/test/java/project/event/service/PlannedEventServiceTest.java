package project.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import project.event.dao.EventManagerRepository;
import project.event.dao.PlannedEventRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.EventManager;
import project.event.model.PlannedEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class PlannedEventServiceTest {

    @Mock
    private PlannedEventRepository plannedEventRepository;

    @Mock
    private EventManagerRepository eventManagerRepository;

    @InjectMocks
    private PlannedEventService plannedEventService;

    private final int PLANNED_EVENT_ID = 1;
    private final int NONEXISTENT_PLANNED_EVENT_ID = 2;
    private final int EVENT_MANAGER_ID = 3;
    private final int NONEXISTENT_EVENT_MANAGER_ID = 4;

    @BeforeEach
    public void setMockOutputPlannedEvent() {
        lenient().when(plannedEventRepository.findPlannedEventByPlannedEventId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(PLANNED_EVENT_ID)) {
                PlannedEvent plannedEvent = new PlannedEvent();
                plannedEvent.setPlannedEventId(PLANNED_EVENT_ID);
                return plannedEvent;
            } else {
                return null;
            }
        });
        lenient().when(eventManagerRepository.findEventManagerByGeneralUserId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(EVENT_MANAGER_ID)) {
                EventManager eventManager = new EventManager();
                eventManager.setGeneralUserId(EVENT_MANAGER_ID);
                return eventManager;
            } else {
                return null;
            }
        });
        lenient().when(plannedEventRepository.findAll()).thenAnswer((invocation) -> {
            ArrayList<PlannedEvent> plannedEvents = new ArrayList<>();
            PlannedEvent plannedEvent = new PlannedEvent();
            plannedEvent.setPlannedEventId(PLANNED_EVENT_ID);
            plannedEvents.add(plannedEvent);
            return plannedEvents;
        });
    }

    /**
     * Test creating a planned event with objects
     */
    @Test
    public void testCreatePlannedEvent() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");
        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    "EventName", "Description", "Location",
                    date);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(plannedEvent);
        assertEquals(eventManager.getGeneralUserId(), plannedEvent.getEventManager().getGeneralUserId());
        assertEquals("EventName", plannedEvent.getEventName());
        assertEquals("Description", plannedEvent.getDescription());
        assertEquals("Location", plannedEvent.getLocation());
        assertEquals(date, plannedEvent.getDate());
    }

    /**
     * Test creating a planned event with a null event manager
     */
    @Test
    public void testCreatePlannedEventNullEventManager() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(null,
                    "EventName", "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Event manager must be instantiated", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a null event name
     */
    @Test
    public void testCreatePlannedEventNullEventName() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");
        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    null, "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Event name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with an empty event name
     */
    @Test
    public void testCreatePlannedEventEmptyEventName() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");
        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    "", "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Event name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a null location
     */
    @Test
    public void testCreatePlannedEventNullLocation() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");
        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    "EventName", "Description", null,
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Location cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with an empty location
     */
    @Test
    public void testCreatePlannedEventEmptyLocation() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");
        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    "EventName", "Description", "",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Location cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a null date
     */
    @Test
    public void testCreatePlannedEventNullDate() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    "EventName", "Description", "Location",
                    null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Date must be instantiated", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a date in the past
     */
    @Test
    public void testCreatePlannedEventPastDate() {
        assertEquals(0, plannedEventRepository.count());

        EventManager eventManager = new EventManager("EventManager");
        GregorianCalendar date = new GregorianCalendar(2020, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(eventManager,
                    "EventName", "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Date cannot be in the past", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with ids
     */
    @Test
    public void testCreatePlannedEventWithIds() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    "EventName", "Description", "Location",
                    date);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(plannedEvent);
        assertEquals(EVENT_MANAGER_ID, plannedEvent.getEventManager().getGeneralUserId());
        assertEquals("EventName", plannedEvent.getEventName());
        assertEquals("Description", plannedEvent.getDescription());
        assertEquals("Location", plannedEvent.getLocation());
        assertEquals(date, plannedEvent.getDate());
    }

    /**
     * Test creating a planned event with a non-existent event manager
     */
    @Test
    public void testCreatePlannedEventNonExistentEventManager() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(NONEXISTENT_EVENT_MANAGER_ID,
                    "EventName", "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Event manager not found", message);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    /**
     * Test creating a planned event with a null event name (id)
     */
    @Test
    public void testCreatePlannedEventNullEventNameId() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    null, "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Event name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with an empty event name (id)
     */
    @Test
    public void testCreatePlannedEventEmptyEventNameId() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    "", "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Event name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a null location (id)
     */
    @Test
    public void testCreatePlannedEventNullLocationId() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    "EventName", "Description", null,
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Location cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with an empty location (id)
     */
    @Test
    public void testCreatePlannedEventEmptyLocationId() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(9000, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    "EventName", "Description", "",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Location cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a null date (id)
     */
    @Test
    public void testCreatePlannedEventNullDateId() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    "EventName", "Description", "Location",
                    null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Date must be instantiated", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a planned event with a date in the past (id)
     */
    @Test
    public void testCreatePlannedEventPastDateId() {
        assertEquals(0, plannedEventRepository.count());

        GregorianCalendar date = new GregorianCalendar(2020, Calendar.MAY, 2);

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.createPlannedEvent(EVENT_MANAGER_ID,
                    "EventName", "Description", "Location",
                    date);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Date cannot be in the past", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a planned event
     */
    @Test
    public void testUpdatePlannedEvent() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    "NewEventName", "NewDescription", "NewLocation",
                    new GregorianCalendar(9000, Calendar.MAY, 3));
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(updatedPlannedEvent);
        assertEquals("NewEventName", updatedPlannedEvent.getEventName());
        assertEquals("NewDescription", updatedPlannedEvent.getDescription());
        assertEquals("NewLocation", updatedPlannedEvent.getLocation());
        assertEquals(new GregorianCalendar(9000, Calendar.MAY, 3), updatedPlannedEvent.getDate());
    }

    /**
     * Test updating a non-existent planned event
     */
    @Test
    public void testUpdatePlannedEventNonExistent() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(NONEXISTENT_PLANNED_EVENT_ID,
                    "NewEventName", "NewDescription", "NewLocation",
                    new GregorianCalendar(9000, Calendar.MAY, 3));
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Planned event not found", message);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    /**
     * Test updating a planned event with a null event name
     */
    @Test
    public void testUpdatePlannedEventNullEventName() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    null, "NewDescription", "NewLocation",
                    new GregorianCalendar(9000, Calendar.MAY, 3));
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Event name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a planned event with an empty event name
     */
    @Test
    public void testUpdatePlannedEventEmptyEventName() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    "", "NewDescription", "NewLocation",
                    new GregorianCalendar(9000, Calendar.MAY, 3));
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Event name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a planned event with a null location
     */
    @Test
    public void testUpdatePlannedEventNullLocation() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    "NewEventName", "NewDescription", null,
                    new GregorianCalendar(9000, Calendar.MAY, 3));
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Location cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a planned event with an empty location
     */
    @Test
    public void testUpdatePlannedEventEmptyLocation() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    "NewEventName", "NewDescription", "",
                    new GregorianCalendar(9000, Calendar.MAY, 3));
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Location cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a planned event with a null date
     */
    @Test
    public void testUpdatePlannedEventNullDate() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    "NewEventName", "NewDescription", "NewLocation",
                    null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Date must be instantiated", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a planned event with a date in the past
     */
    @Test
    public void testUpdatePlannedEventPastDate() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent updatedPlannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            updatedPlannedEvent = plannedEventService.updatePlannedEvent(PLANNED_EVENT_ID,
                    "NewEventName", "NewDescription", "NewLocation",
                    new GregorianCalendar(2020, Calendar.MAY, 3));
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(updatedPlannedEvent);
        assertEquals("Date cannot be in the past", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test deleting a planned event
     */
    @Test
    public void testDeletePlannedEvent() {
        assertEquals(0, plannedEventRepository.count());

        boolean deleted = false;
        try {
            deleted = plannedEventService.deletePlannedEvent(PLANNED_EVENT_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertTrue(deleted);
    }

    /**
     * Test deleting a non-existent planned event
     */
    @Test
    public void testDeletePlannedEventNonExistent() {
        assertEquals(0, plannedEventRepository.count());

        boolean deleted = false;
        String message = null;
        HttpStatus status = null;
        try {
            deleted = plannedEventService.deletePlannedEvent(NONEXISTENT_PLANNED_EVENT_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertFalse(deleted);
        assertEquals("Planned event not found", message);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    /**
     * Test getting a planned event by its id
     */
    @Test
    public void testGetPlannedEventById() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent plannedEvent = null;
        try {
            plannedEvent = plannedEventService.getPlannedEventById(PLANNED_EVENT_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(plannedEvent);
        assertEquals(PLANNED_EVENT_ID, plannedEvent.getPlannedEventId());
    }

    /**
     * Test getting a non-existent planned event by its id
     */
    @Test
    public void testGetPlannedEventByIdNonExistent() {
        assertEquals(0, plannedEventRepository.count());

        PlannedEvent plannedEvent = null;
        String message = null;
        HttpStatus status = null;
        try {
            plannedEvent = plannedEventService.getPlannedEventById(NONEXISTENT_PLANNED_EVENT_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(plannedEvent);
        assertEquals("Planned event not found", message);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    /**
     * Test getting all planned events
     */
    @Test
    public void testGetAllPlannedEvents() {
        assertEquals(0, plannedEventRepository.count());

        ArrayList<PlannedEvent> plannedEvents = plannedEventService.getAllPlannedEvents();

        assertNotNull(plannedEvents);
        assertEquals(1, plannedEvents.size());
        assertEquals(PLANNED_EVENT_ID, plannedEvents.get(0).getPlannedEventId());
    }

}
