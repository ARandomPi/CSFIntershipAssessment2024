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
public class PlannedEventRepositoryTest {

    @Autowired
    private PlannedEventRepository plannedEventRepository;

    @Autowired
    private EventManagerRepository eventManagerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        plannedEventRepository.deleteAll();
        eventManagerRepository.deleteAll();
    }

    /**
     * Test the persistence and retrieval of the PlannedEvent object.
     */
    @Test
    public void testPersistAndReadPlannedEvent() {

        EventManager eventManager = new EventManager("nameM");
        EventManager generatedEventManager = eventManagerRepository.save(eventManager);

        GregorianCalendar calendar = new GregorianCalendar(2021, Calendar.MAY, 1);
        PlannedEvent plannedEvent = new PlannedEvent(generatedEventManager, "event", "description", "location",
                calendar);
        PlannedEvent generated = plannedEventRepository.save(plannedEvent);

        PlannedEvent returned = plannedEventRepository.findPlannedEventByPlannedEventId(generated.getPlannedEventId());

        assertNotNull(returned);

        // Check original data
        assertEquals(generatedEventManager.getGeneralUserId(), returned.getEventManager().getGeneralUserId());
        assertEquals("event", returned.getEventName());
        assertEquals("description", returned.getDescription());
        assertEquals("location", returned.getLocation());
        assertEquals(calendar, returned.getDate());

        // Check post-save data
        assertEquals(generated.getPlannedEventId(), returned.getPlannedEventId());
        assertEquals(generated.getEventManager().getGeneralUserId(), returned.getEventManager().getGeneralUserId());
        assertEquals(generated.getEventName(), returned.getEventName());
        assertEquals(generated.getDescription(), returned.getDescription());
        assertEquals(generated.getLocation(), returned.getLocation());
        assertEquals(generated.getDate(), returned.getDate());
    }
}
