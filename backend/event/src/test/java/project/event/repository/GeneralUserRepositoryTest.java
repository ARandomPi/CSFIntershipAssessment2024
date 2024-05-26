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
public class GeneralUserRepositoryTest {

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        generalUserRepository.deleteAll();
    }

    /**
     * Test the persistence and retrieval of the GeneralUser object.
     */
    @Test
    public void testPersistAndReadGeneralUser() {

        GeneralUser generalUser = new GeneralUser("name");
        GeneralUser generated = generalUserRepository.save(generalUser);

        GeneralUser returned = generalUserRepository.findGeneralUserByGeneralUserId(generated.getGeneralUserId());

        assertNotNull(returned);

        // Check original data
        assertEquals(generalUser.getName(), returned.getName());

        // Check post-save data
        assertEquals(generated.getGeneralUserId(), returned.getGeneralUserId());
        assertEquals(generated.getName(), returned.getName());
    }
}
