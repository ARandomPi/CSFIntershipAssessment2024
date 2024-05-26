package project.event.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.event.dao.EventManagerRepository;
import project.event.dao.GeneralUserRepository;
import project.event.exception.EventRegistrationAppException;
import project.event.model.GeneralUser;

import java.util.ArrayList;

@Service
public class GeneralUserService {

    @Autowired
    private GeneralUserRepository generalUserRepository;

    @Autowired
    private EventManagerRepository eventManagerRepository;

    /**
     * Create a general user
     * @param name the name of the general user
     * @return the general user created
     */
    @Transactional
    public GeneralUser createGeneralUser(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EventRegistrationAppException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        // Checking if there is a duplicate name
        ArrayList<GeneralUser> generalUsers = new ArrayList<>(generalUserRepository.findAll());
        generalUsers.addAll(eventManagerRepository.findAll());
        for(GeneralUser generalUser : generalUsers) {
            if (generalUser.getName().equals(name)) {
                throw new EventRegistrationAppException("Name already exists", HttpStatus.BAD_REQUEST);
            }
        }
        GeneralUser generalUser = new GeneralUser(name);
        generalUserRepository.save(generalUser);
        return generalUser;
    }

    /**
     * Update a general user
     * @param generalUserId the id of the general user
     * @param name the new name of the general user
     * @return the general user updated
     */
    @Transactional
    public GeneralUser updateGeneralUser(int generalUserId, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EventRegistrationAppException("Name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        GeneralUser generalUser = generalUserRepository.findGeneralUserByGeneralUserId(generalUserId);
        if (generalUser == null) {
            throw new EventRegistrationAppException("General user not found", HttpStatus.BAD_REQUEST);
        }
        // Checking if there is a duplicate name
        ArrayList<GeneralUser> generalUsers = new ArrayList<>(generalUserRepository.findAll());
        generalUsers.addAll(eventManagerRepository.findAll());
        for(GeneralUser user : generalUsers) {
            if (user.getName().equals(name)) {
                throw new EventRegistrationAppException("Name already exists", HttpStatus.BAD_REQUEST);
            }
        }
        generalUser.setName(name);
        generalUserRepository.save(generalUser);
        return generalUser;
    }

    /**
     * Delete a general user
     * @param generalUserId the id of the general user
     * @return true if the general user is deleted
     */
    @Transactional
    public boolean deleteGeneralUser(int generalUserId) {
        GeneralUser generalUser = generalUserRepository.findGeneralUserByGeneralUserId(generalUserId);
        if (generalUser == null) {
            throw new EventRegistrationAppException("General user not found", HttpStatus.BAD_REQUEST);
        }
        try {
            generalUserRepository.delete(generalUser);
        } catch (Exception e) {
            throw new EventRegistrationAppException("General user cannot be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    /**
     * Get a general user by id
     * @param generalUserId the id of the general user
     * @return the general user found
     */
    @Transactional
    public GeneralUser getGeneralUser(int generalUserId) {
        GeneralUser generalUser = generalUserRepository.findGeneralUserByGeneralUserId(generalUserId);
        if (generalUser == null) {
            throw new EventRegistrationAppException("General user not found", HttpStatus.NOT_FOUND);
        }
        return generalUser;
    }

    /**
     * Get all general users
     * @return an ArrayList of all general users
     */
    @Transactional
    public ArrayList<GeneralUser> getAllGeneralUsers() {
        return new ArrayList<>(generalUserRepository.findAll());
    }
}
