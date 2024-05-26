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
public class GeneralUserServiceTest {

    @Mock
    private GeneralUserRepository generalUserRepository;

    @Mock
    private EventManagerRepository eventManagerRepository;

    @InjectMocks
    private GeneralUserService generalUserService;

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
     * Test creating a general user
     */
    @Test
    public void testCreateGeneralUser() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        try {
            generalUser = generalUserService.createGeneralUser("name10");
        } catch (EventRegistrationAppException e) {
            fail();
        }
        assertNotNull(generalUser);
        assertEquals("name10", generalUser.getName());
    }

    /**
     * Test creating a general user with a null name
     */
    @Test
    public void testCreateGeneralUserNullName() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.createGeneralUser(null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a general user with an empty name
     */
    @Test
    public void testCreateGeneralUserEmptyName() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.createGeneralUser("");
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test creating a general user with a duplicate name
     */
    @Test
    public void testCreateGeneralUserDuplicateName() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.createGeneralUser(NAME_MANAGER);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("Name already exists", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a general user
     */
    @Test
    public void testUpdateGeneralUser() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        try {
            generalUser = generalUserService.updateGeneralUser(USER_ID, "newName");
        } catch (EventRegistrationAppException e) {
            fail();
        }
        assertNotNull(generalUser);
        assertEquals("newName", generalUser.getName());
    }

    /**
     * Test updating a general user with a null name
     */
    @Test
    public void testUpdateGeneralUserNullName() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.updateGeneralUser(USER_ID, null);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a general user with an empty name
     */
    @Test
    public void testUpdateGeneralUserEmptyName() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.updateGeneralUser(USER_ID, "");
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("Name cannot be empty", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a general user with a duplicate name
     */
    @Test
    public void testUpdateGeneralUserDuplicateName() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.updateGeneralUser(USER_ID, NAME_MANAGER);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("Name already exists", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test updating a general user that does not exist
     */
    @Test
    public void testUpdateGeneralUserNonExisting() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.updateGeneralUser(NON_EXISTING_USER_ID, "newName");
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("General user not found", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Test deleting a general user
     */
    @Test
    public void testDeleteGeneralUser() {
        assertEquals(0, generalUserRepository.count());

        boolean deleted = false;
        try {
            deleted = generalUserService.deleteGeneralUser(USER_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }
        assertTrue(deleted);
        assertEquals(0, generalUserRepository.count());
    }

    /**
     * Test deleting a general user that does not exist
     */
    @Test
    public void testDeleteGeneralUserNonExisting() {
        assertEquals(0, generalUserRepository.count());

        boolean deleted = false;
        String message = null;
        HttpStatus status = null;
        try {
            deleted = generalUserService.deleteGeneralUser(MANAGER_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertFalse(deleted);
        assertEquals("General user not found", message);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    /**
     * Get a general user by its id
     */
    @Test
    public void testGetGeneralUser() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        try {
            generalUser = generalUserService.getGeneralUser(USER_ID);
        } catch (EventRegistrationAppException e) {
            fail();
        }
        assertNotNull(generalUser);
        assertEquals(NAME_USER, generalUser.getName());
    }

    /**
     * Get a general user that does not exist
     */
    @Test
    public void testGetGeneralUserNonExisting() {
        assertEquals(0, generalUserRepository.count());

        GeneralUser generalUser = null;
        String message = null;
        HttpStatus status = null;
        try {
            generalUser = generalUserService.getGeneralUser(NON_EXISTING_USER_ID);
            fail();
        } catch (EventRegistrationAppException e) {
            message = e.getMessage();
            status = e.getStatus();
        }
        assertNull(generalUser);
        assertEquals("General user not found", message);
        assertEquals(HttpStatus.NOT_FOUND, status);
    }

    /**
     * Test getting all general users
     */
    @Test
    public void testGetAllGeneralUsers() {
        assertEquals(0, generalUserRepository.count());

        ArrayList<GeneralUser> generalUsers = null;
        try {
            generalUsers = generalUserService.getAllGeneralUsers();
        } catch (EventRegistrationAppException e) {
            fail();
        }
        assertNotNull(generalUsers);
        assertEquals(1, generalUsers.size());
        assertEquals(NAME_USER, generalUsers.get(0).getName());
    }

}
