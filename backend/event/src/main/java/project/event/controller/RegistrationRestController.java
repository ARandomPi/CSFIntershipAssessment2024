package project.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.event.dto.*;
import project.event.exception.EventRegistrationAppException;
import project.event.model.Registration;
import project.event.service.RegistrationService;

import java.util.Calendar;
import java.util.List;

@CrossOrigin("*")
@RestController
public class RegistrationRestController {

    @Autowired
    private RegistrationService registrationService;

    // Get mappings
    @GetMapping(value = { "/registration/{id}", "/registration/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponseDto getRegistrationById(@PathVariable("id") int id) {
        Registration registration = registrationService.getRegistrationById(id);
        return convertToDto(registration);
    }

    @GetMapping(value = { "/registration", "/registration/" })
    @ResponseStatus(HttpStatus.OK)
    public List<RegistrationResponseDto> getAllRegistrations() {
        List<Registration> registrations = registrationService.getAllRegistrations();
        return registrations.stream().map(this::convertToDto).toList();
    }

    // Post mapping
    @PostMapping(value = { "/registration", "/registration/" })
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDto createRegistration(@RequestBody RegistrationRequestDto registrationRequestDto) {
        Registration registration = registrationService.createRegistration(
                registrationRequestDto.getPlannedEvent().getEventId(),
                registrationRequestDto.getGeneralUser().getGeneralUserId()
        );
        return convertToDto(registration);
    }

    // Delete mapping
    @DeleteMapping(value = { "/registration/{id}", "/registration/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public void deleteRegistration(@PathVariable("id") int id) {
        registrationService.deleteRegistration(id);
    }



    /**
     * Helper method to convert Registration objects into DTOs
     */
    private RegistrationResponseDto convertToDto(Registration registration) {
        if (registration == null) {
            throw new EventRegistrationAppException("Registration not found", HttpStatus.NOT_FOUND);
        }
        GeneralUserResponseDto generalUserResponseDto = new GeneralUserResponseDto (
                registration.getGeneralUser().getName(),
                registration.getGeneralUser().getGeneralUserId()
        );
        EventManagerResponseDto eventManagerResponseDto = new EventManagerResponseDto(
                registration.getPlannedEvent().getEventManager().getName(),
                registration.getPlannedEvent().getEventManager().getGeneralUserId()
        );
        PlannedEventResponseDto plannedEventResponseDto = new PlannedEventResponseDto(
                eventManagerResponseDto,
                registration.getPlannedEvent().getEventName(),
                registration.getPlannedEvent().getDescription(),
                registration.getPlannedEvent().getLocation(),
                registration.getPlannedEvent().getDate().get(Calendar.YEAR),
                registration.getPlannedEvent().getDate().get(Calendar.MONTH),
                registration.getPlannedEvent().getDate().get(Calendar.DAY_OF_MONTH),
                registration.getPlannedEvent().getPlannedEventId()
        );
        return new RegistrationResponseDto(
                plannedEventResponseDto,
                generalUserResponseDto,
                registration.getRegistrationId()
        );
    }
}
