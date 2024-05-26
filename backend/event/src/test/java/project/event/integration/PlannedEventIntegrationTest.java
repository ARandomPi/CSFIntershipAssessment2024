package project.event.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.event.dao.EventManagerRepository;
import project.event.dao.PlannedEventRepository;
import project.event.dto.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlannedEventIntegrationTest {

    @Autowired
    private TestRestTemplate assignmentClient;

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
     * Helper method to create an EventManager object.
     * @return the generated EventManager's response DTO
     */
    private EventManagerResponseDto createEventManager() {
        EventManagerRequestDto eventManagerRequestDto = new EventManagerRequestDto("manager");
        ResponseEntity<EventManagerResponseDto> response = assignmentClient.postForEntity(
                "/eventManager", eventManagerRequestDto, EventManagerResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    /**
     * Helper method to create a PlannedEvent object.
     * @param eventManagerResponseDto the ID of the EventManager who is creating the event
     * @return the generated PlannedEvent's response DTO
     */
    private PlannedEventResponseDto createPlannedEvent(EventManagerResponseDto eventManagerResponseDto) {
        PlannedEventRequestDto plannedEventRequestDto = new PlannedEventRequestDto(
                eventManagerResponseDto, "event", "description", "location", 2025, 5, 1);
        ResponseEntity<PlannedEventResponseDto> response = assignmentClient.postForEntity(
                "/plannedEvent", plannedEventRequestDto, PlannedEventResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    /**
     * Test the creation of a PlannedEvent object.
     */
    @Test
    public void testCreatePlannedEvent() {
        // Creating all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        assertNotNull(plannedEventResponseDto);

        // Checking the response
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
    }

    /**
     * Test the deletion of a PlannedEvent object.
     */
    @Test
    public void testDeletePlannedEvent() {
        // Creating all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        assertNotNull(plannedEventResponseDto);

        // Deleting the PlannedEvent
        assignmentClient.delete("/plannedEvent/" + plannedEventResponseDto.getEventId());

        // Checking the response
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(0, plannedEventRepository.count());
    }

    /**
     * Test the update of a PlannedEvent object.
     */
    @Test
    public void testUpdatePlannedEvent() {
        // Creating all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        assertNotNull(plannedEventResponseDto);

        // Updating the PlannedEvent
        PlannedEventRequestDto plannedEventRequestDto = new PlannedEventRequestDto(
                eventManagerResponseDto, "new event", "new description",
                "new location", 2025, 5, 1);
        assignmentClient.put("/plannedEvent/" + plannedEventResponseDto.getEventId(), plannedEventRequestDto);
        ResponseEntity<PlannedEventResponseDto> response = assignmentClient.getForEntity(
                "/plannedEvent/" + plannedEventResponseDto.getEventId(), PlannedEventResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Checking the response
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
        assertEquals("new event", plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getEventName());
        assertEquals("new description", plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getDescription());
        assertEquals("new location", plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getLocation());
    }

    /**
     * Test the retrieval of one PlannedEvent object.
     */
    @Test
    public void testGetPlannedEvent() {
        // Creating all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        assertNotNull(plannedEventResponseDto);

        // Retrieving the PlannedEvent
        ResponseEntity<PlannedEventResponseDto> response = assignmentClient.getForEntity(
                "/plannedEvent/" + plannedEventResponseDto.getEventId(), PlannedEventResponseDto.class);

        // Checking the response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
    }

    /**
     * Test the retrieval of all PlannedEvent objects.
     */
    @Test
    public void testGetAllPlannedEvents() {
        // Creating all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        assertNotNull(plannedEventResponseDto);

        // Retrieving all PlannedEvents
        ResponseEntity<List<PlannedEventResponseDto>> response = assignmentClient.exchange(
                "/plannedEvent/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PlannedEventResponseDto>>() {});

        // Checking the response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
    }
}
