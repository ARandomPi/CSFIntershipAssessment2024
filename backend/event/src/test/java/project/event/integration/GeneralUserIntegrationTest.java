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
import project.event.dao.GeneralUserRepository;
import project.event.dto.GeneralUserRequestDto;
import project.event.dto.GeneralUserResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeneralUserIntegrationTest {

    @Autowired
    private TestRestTemplate assignmentClient;

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        generalUserRepository.deleteAll();
    }

    /**
     * Helper method to create a GeneralUser object.
     * @return the generated GeneralUser's response DTO
     */
    private GeneralUserResponseDto createGeneralUser() {
        GeneralUserRequestDto generalUserRequestDto = new GeneralUserRequestDto("user");
        ResponseEntity<GeneralUserResponseDto> response = assignmentClient.postForEntity(
                "/generalUser", generalUserRequestDto, GeneralUserResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    /**
     * Test to create a GeneralUser object.
     */
    @Test
    public void testCreateGeneralUser() {
        // Create all necessary objects
        GeneralUserResponseDto generalUserResponseDto = createGeneralUser();

        // Check the response
        assertEquals(generalUserResponseDto.getGeneralUserId(), generalUserRepository.findGeneralUserByGeneralUserId(
                generalUserResponseDto.getGeneralUserId()).getGeneralUserId());
    }

    /**
     * Test to delete a GeneralUser object.
     */
    @Test
    public void testDeleteGeneralUser() {
        // Create all necessary objects
        GeneralUserResponseDto generalUserResponseDto = createGeneralUser();

        // Delete the object
        assignmentClient.delete("/generalUser/" + generalUserResponseDto.getGeneralUserId());

        // Check if the object was deleted
        assertEquals(0, generalUserRepository.count());
    }

    /**
     * Test to update a GeneralUser object.
     */
    @Test
    public void testUpdateGeneralUser() {
        // Create all necessary objects
        GeneralUserResponseDto generalUserResponseDto = createGeneralUser();

        // Update the object
        GeneralUserRequestDto generalUserRequestDto = new GeneralUserRequestDto("newUser");
        assignmentClient.put("/generalUser/" + generalUserResponseDto.getGeneralUserId(), generalUserRequestDto);
        ResponseEntity<GeneralUserResponseDto> response = assignmentClient.getForEntity(
                "/generalUser/" + generalUserResponseDto.getGeneralUserId(), GeneralUserResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Check if the object was updated
        assertEquals("newUser", generalUserRepository.findGeneralUserByGeneralUserId(
                generalUserResponseDto.getGeneralUserId()).getName());
    }

    /**
     * Test to get a GeneralUser object.
     */
    @Test
    public void testGetGeneralUser() {
        // Create all necessary objects
        GeneralUserResponseDto generalUserResponseDto = createGeneralUser();

        // Get the object
        ResponseEntity<GeneralUserResponseDto> response = assignmentClient.getForEntity(
                "/generalUser/" + generalUserResponseDto.getGeneralUserId(), GeneralUserResponseDto.class);

        // Check the response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(generalUserResponseDto.getGeneralUserId(), generalUserRepository.findGeneralUserByGeneralUserId(
                generalUserResponseDto.getGeneralUserId()).getGeneralUserId());
    }

    /**
     * Test to get all GeneralUser objects.
     */
    @Test
    public void testGetAllGeneralUsers() {
        // Create all necessary objects
        GeneralUserResponseDto generalUserResponseDto = createGeneralUser();

        // Get all objects
        ResponseEntity<List<GeneralUserResponseDto>> response = assignmentClient.exchange(
                "/generalUser/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GeneralUserResponseDto>>() {});

        // Check the response
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(generalUserResponseDto.getGeneralUserId(), generalUserRepository.findGeneralUserByGeneralUserId(
                generalUserResponseDto.getGeneralUserId()).getGeneralUserId());
    }


}
