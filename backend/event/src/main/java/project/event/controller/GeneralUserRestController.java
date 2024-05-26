package project.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.event.dto.GeneralUserRequestDto;
import project.event.dto.GeneralUserResponseDto;
import project.event.exception.EventRegistrationAppException;
import project.event.service.GeneralUserService;
import project.event.model.GeneralUser;

import java.util.List;

@CrossOrigin("*")
@RestController
public class GeneralUserRestController {

    @Autowired
    private GeneralUserService generalUserService;

    // Get mappings
    @GetMapping(value = { "/generalUser/{id}", "/generalUser/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public GeneralUserResponseDto getGeneralUserById(@PathVariable("id") int id) {
        GeneralUser generalUser = generalUserService.getGeneralUser(id);
        return convertToDto(generalUser);
    }

    @GetMapping(value = { "/generalUser", "/generalUser/" })
    @ResponseStatus(HttpStatus.OK)
    public List<GeneralUserResponseDto> getAllGeneralUsers() {
        List<GeneralUser> generalUsers = generalUserService.getAllGeneralUsers();
        return generalUsers.stream().map(this::convertToDto).toList();
    }

    // Post mapping
    @PostMapping(value = { "/generalUser", "/generalUser/" })
    @ResponseStatus(HttpStatus.CREATED)
    public GeneralUserResponseDto createGeneralUser(@RequestBody GeneralUserRequestDto generalUserResponseDto) {
        GeneralUser generalUser = generalUserService.createGeneralUser(
                generalUserResponseDto.getName()
        );
        return convertToDto(generalUser);
    }

    // Put mapping
    @PutMapping(value = { "/generalUser/{id}", "/generalUser/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public GeneralUserResponseDto updateGeneralUser(@PathVariable("id") int id,
                                                    @RequestBody GeneralUserRequestDto generalUserResponseDto) {
        GeneralUser generalUser = generalUserService.updateGeneralUser(
                id,
                generalUserResponseDto.getName()
        );
        return convertToDto(generalUser);
    }

    // Delete mapping
    @DeleteMapping(value = { "/generalUser/{id}", "/generalUser/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public void deleteGeneralUser(@PathVariable("id") int id) {
        generalUserService.deleteGeneralUser(id);
    }

    /**
     * Helper method to convert GeneralUser objects into DTOs
     */
    private GeneralUserResponseDto convertToDto(GeneralUser generalUser) {
        if (generalUser == null) {
            throw new EventRegistrationAppException("GeneralUser not found", HttpStatus.NOT_FOUND);
        }
        return new GeneralUserResponseDto(
                generalUser.getName(),
                generalUser.getGeneralUserId()
        );
    }
}
