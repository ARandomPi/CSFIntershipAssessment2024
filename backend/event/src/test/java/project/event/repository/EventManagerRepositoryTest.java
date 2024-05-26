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
public class EventManagerRepositoryTest {

    @Autowired
    private EventManagerRepository eventManagerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        eventManagerRepository.deleteAll();
    }

    /**
     * Test the persistence and retrieval of a single EventManager object.
     */
    @Test
    public void testPersistAndReadEventManager() {
        String name = "name";

        EventManager eventManager = new EventManager(name);

        EventManager generated = eventManagerRepository.save(eventManager);

        EventManager returned = eventManagerRepository.findEventManagerByGeneralUserId(generated.getGeneralUserId());

        assertNotNull(returned);

        // Check original data
        assertEquals(name, returned.getName());

        // Check post-save data
        assertEquals(generated.getGeneralUserId(), returned.getGeneralUserId());
        assertEquals(generated.getName(), returned.getName());
    }
}
