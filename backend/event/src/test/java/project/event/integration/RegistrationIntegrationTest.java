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
import project.event.dao.*;
import project.event.dto.*;
import project.event.model.GeneralUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationIntegrationTest {

    @Autowired
    private TestRestTemplate assignmentClient;

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
     * Helper method to create a Registration object using an EventManager and a PlannedEvent.
     * @param eventManagerResponseDto the ID of the EventManager who is creating the event
     * @param plannedEventResponseDto the ID of the PlannedEvent being registered for
     * @return the generated Registration's response DTO
     */
    private RegistrationResponseDto createRegistration(EventManagerResponseDto eventManagerResponseDto,
                                                        PlannedEventResponseDto plannedEventResponseDto) {
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto(
                plannedEventResponseDto, eventManagerResponseDto);
        ResponseEntity<RegistrationResponseDto> response = assignmentClient.postForEntity(
                "/registration", registrationRequestDto, RegistrationResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    /**
     * Test the creation of a Registration object.
     */
    @Test
    void testCreateRegistration() {
        // Creating all the necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        RegistrationResponseDto registrationResponseDto = createRegistration(
                eventManagerResponseDto, plannedEventResponseDto
        );

        // Check the response
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                        eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                        plannedEventResponseDto.getEventId()).getPlannedEventId());
        assertEquals(registrationResponseDto.getRegistrationId(), registrationRepository.findRegistrationByRegistrationId(
                        registrationResponseDto.getRegistrationId()).getRegistrationId());
    }

    /**
     * Test the deletion of a Registration object.
     */
    @Test
    void testDeleteRegistration() {
        // Creating all the necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        RegistrationResponseDto registrationResponseDto = createRegistration(
                eventManagerResponseDto, plannedEventResponseDto
        );

        // Deleting the registration
        assignmentClient.delete("/registration/" + registrationResponseDto.getRegistrationId());

        // Check the response
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
        assertEquals(0, registrationRepository.findAll().size());
    }

    /**
     * Test the retrieval of one Registration objects.
     */
    @Test
    void testGetRegistration() {
        // Creating all the necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        RegistrationResponseDto registrationResponseDto = createRegistration(
                eventManagerResponseDto, plannedEventResponseDto
        );

        // Retrieving the registration
        ResponseEntity<RegistrationResponseDto> response = assignmentClient.getForEntity(
                "/registration/" + registrationResponseDto.getRegistrationId(), RegistrationResponseDto.class);

        // Check the response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
        assertEquals(registrationResponseDto.getRegistrationId(), registrationRepository.findRegistrationByRegistrationId(
                registrationResponseDto.getRegistrationId()).getRegistrationId());
    }

    /**
     * Test the retrieval of all Registration objects.
     */
    @Test
    void testGetAllRegistrations() {
        // Creating all the necessary objects
        EventManagerResponseDto eventManagerResponseDto = createEventManager();
        PlannedEventResponseDto plannedEventResponseDto = createPlannedEvent(eventManagerResponseDto);
        RegistrationResponseDto registrationResponseDto = createRegistration(
                eventManagerResponseDto, plannedEventResponseDto
        );

        // Retrieving all registrations
        ResponseEntity<List<RegistrationResponseDto>> response = assignmentClient.exchange(
                "/registration/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RegistrationResponseDto>>() {});

        // Check the response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(eventManagerResponseDto.getGeneralUserId(), eventManagerRepository.findEventManagerByGeneralUserId(
                eventManagerResponseDto.getGeneralUserId()).getGeneralUserId());
        assertEquals(plannedEventResponseDto.getEventId(), plannedEventRepository.findPlannedEventByPlannedEventId(
                plannedEventResponseDto.getEventId()).getPlannedEventId());
        assertEquals(registrationResponseDto.getRegistrationId(), registrationRepository.findRegistrationByRegistrationId(
                registrationResponseDto.getRegistrationId()).getRegistrationId());
    }
}
