package project.event.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.event.dao.EventManagerRepository;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import project.event.dto.EventManagerRequestDto;
import project.event.dto.EventManagerResponseDto;
import project.event.dto.GeneralUserResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventManagerIntegrationTest {

    @Autowired
    private TestRestTemplate assignmentClient;

    @Autowired
    private EventManagerRepository eventManagerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
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
     * Test to create an EventManager object.
     */
    @Test
    public void testCreateEventManager() {
        // Create all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();

        // Check the response
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
    }

    /**
     * Test to delete an EventManager object.
     */
    @Test
    public void testDeleteEventManager() {
        // Create all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();

        // Delete the EventManager
        assignmentClient.delete("/eventManager/" + eventManagerResponseDto.getGeneralUserId());

        // Check response
        assertEquals(0, eventManagerRepository.count());
    }

    /**
     * Test to update an EventManager object.
     */
    @Test
    public void testUpdateEventManager() {
        // Create all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();

        // Update the EventManager
        EventManagerRequestDto eventManagerRequestDto = new EventManagerRequestDto("newManager");
        assignmentClient.put("/eventManager/" + eventManagerResponseDto.getGeneralUserId(), eventManagerRequestDto);
        ResponseEntity<EventManagerRequestDto> response = assignmentClient.getForEntity(
                "/generalUser/" + eventManagerResponseDto.getGeneralUserId(), EventManagerRequestDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check response
        assertEquals("newManager", eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getName());
    }

    /**
     * Test to get an EventManager object.
     */
    @Test
    public void testGetEventManager() {
        // Create all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();

        // Get the EventManager
        ResponseEntity<EventManagerResponseDto> response = assignmentClient.getForEntity(
                "/eventManager/" + eventManagerResponseDto.getGeneralUserId(), EventManagerResponseDto.class);

        // Check response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
    }

    /**
     * Test to get all EventManager objects.
     */
    @Test
    public void testGetAllEventManagers() {
        // Create all necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();

        // Get all EventManagers
        ResponseEntity<List<EventManagerResponseDto>> response = assignmentClient.exchange(
                "/eventManager/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<EventManagerResponseDto>>() {});

        // Check response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
    }
}
