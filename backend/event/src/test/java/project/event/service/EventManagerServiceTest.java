package project.event.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import project.event.dao.GeneralUserRepository;
import project.event.dao.EventManagerRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.EventManager;
import project.event.model.GeneralUser;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class EventManagerServiceTest {

    @Mock
    private GeneralUserRepository generalUserRepository;

    @Mock
    private EventManagerRepository eventManagerRepository;

    @InjectMocks
    private EventManagerService eventManagerService;

    private final String NAME_USER = "name1";
    private final String NAME_MANAGER = "name2";
    private final int USER_ID = 1;
    private final int MANAGER_ID = 2;
    private final int NON_EXISTING_USER_ID = 3;
    private final int NON_EXISTING_MANAGER_ID = 4;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(generalUserRepository.findGeneralUserByGeneralUserId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(USER_ID)) {
                GeneralUser generalUser = new GeneralUser();
                generalUser.setGeneralUserId(USER_ID);
                generalUser.setName(NAME_USER);
                return generalUser;
            } else {
                return null;
            }
        });

        lenient().when(eventManagerRepository.findEventManagerByGeneralUserId(anyInt())).thenAnswer((invocation) -> {
            if (invocation.getArgument(0).equals(MANAGER_ID)) {
                EventManager eventManager = new EventManager();
                eventManager.setGeneralUserId(MANAGER_ID);
                eventManager.setName(NAME_MANAGER);
                return eventManager;
            } else {
                return null;
            }
        });
        lenient().when(eventManagerRepository.findAll()).thenAnswer((invocation) -> {
            ArrayList<EventManager> eventManagers = new ArrayList<>();
            EventManager eventManager = new EventManager();
            eventManager.setGeneralUserId(MANAGER_ID);
            eventManager.setName(NAME_MANAGER);
            eventManagers.add(eventManager);
            return eventManagers;
        });
        lenient().when(generalUserRepository.findAll()).thenAnswer((invocation) -> {
            ArrayList<GeneralUser> generalUsers = new ArrayList<>();
            GeneralUser generalUser = new GeneralUser();
            generalUser.setGeneralUserId(USER_ID);
            generalUser.setName(NAME_USER);
            generalUsers.add(generalUser);
            return generalUsers;
        });
    }

    /**
     * Test for creating an event manager
     */
    @Test
    public void testCreateEventManager() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        try {
            eventManager = eventManagerService.createEventManager("manager");
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(eventManager);
        assertEquals("manager", eventManager.getName());
    }

    /**
     * Test for creating an event manager with a null name
     */
    @Test
    public void testCreateEventManagerNullName() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.createEventManager(null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for creating an event manager with an empty name
     */
    @Test
    public void testCreateEventManagerEmptyName() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.createEventManager("");
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for creating an event manager with a duplicate name
     */
    @Test
    public void testCreateEventManagerDuplicateName() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.createEventManager(NAME_USER);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Name already exists", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for updating an event manager
     */
    @Test
    public void testUpdateEventManager() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        try {
            eventManager = eventManagerService.updateEventManager(MANAGER_ID, "newName");
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(eventManager);
        assertEquals("newName", eventManager.getName());
    }

    /**
     * Test for updating an event manager with a null name
     */
    @Test
    public void testUpdateEventManagerNullName() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.updateEventManager(MANAGER_ID, null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for updating an event manager with an empty name
     */
    @Test
    public void testUpdateEventManagerEmptyName() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.updateEventManager(MANAGER_ID, "");
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for updating an event manager with a duplicate name
     */
    @Test
    public void testUpdateEventManagerDuplicateName() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.updateEventManager(MANAGER_ID, NAME_USER);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Name already exists", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for updating an event manager that does not exist
     */
    @Test
    public void testUpdateEventManagerNonExisting() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.updateEventManager(NON_EXISTING_MANAGER_ID, "newName");
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Event manager not found", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test for deleting an event manager
     */
    @Test
    public void testDeleteEventManager() {
        assertEquals(0, eventManagerRepository.count());

        boolean deleted = false;
        try {
            deleted = eventManagerService.deleteEventManager(MANAGER_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertTrue(deleted);
        assertEquals(0, eventManagerRepository.count());
    }

    /**
     * Test for deleting an event manager that does not exist
     */
    @Test
    public void testDeleteEventManagerNonExisting() {
        assertEquals(0, eventManagerRepository.count());

        boolean deleted = false;
        String message = null;
        HttpStatus status = null;
        try {
            deleted = eventManagerService.deleteEventManager(NON_EXISTING_MANAGER_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertFalse(deleted);
        assertEquals("Event manager not found", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test getting an event manager
     */
    @Test
    public void testGetEventManager() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        try {
            eventManager = eventManagerService.getEventManagerById(MANAGER_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(eventManager);
        assertEquals(NAME_MANAGER, eventManager.getName());
    }

    /**
     * Test getting an event manager that does not exist
     */
    @Test
    public void testGetEventManagerNonExisting() {
        assertEquals(0, eventManagerRepository.count());

        EventManager eventManager = null;
        String message = null;
        HttpStatus status = null;
        try {
            eventManager = eventManagerService.getEventManagerById(NON_EXISTING_MANAGER_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }

        assertNull(eventManager);
        assertEquals("Event manager not found", message);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    /**
     * Test getting all event managers
     */
    @Test
    public void testGetAllEventManagers() {
        assertEquals(0, eventManagerRepository.count());

        ArrayList<EventManager> eventManagers = null;
        try {
            eventManagers = eventManagerService.getAllEventManagers();
        } catch (EventRegistrationAppException e) {
            fail();
        }

        assertNotNull(eventManagers);
        assertEquals(1, eventManagers.size());
        assertEquals(NAME_MANAGER, eventManagers.get(0).getName());
    }

}
