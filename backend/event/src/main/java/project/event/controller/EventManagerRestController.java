package project.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.event.dto.EventManagerRequestDto;
import project.event.dto.EventManagerResponseDto;
import project.event.exception.EventRegistrationAppException;
import project.event.service.EventManagerService;
import project.event.model.EventManager;

import java.util.List;

@CrossOrigin("*")
@RestController
public class EventManagerRestController {

    @Autowired
    private EventManagerService eventManagerService;

    // Get mappings
    @GetMapping(value = { "/eventManager/{id}", "/eventManager/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public EventManagerResponseDto getEventManagerById(@PathVariable("id") int id) {
        EventManager eventManager = eventManagerService.getEventManagerById(id);
        return convertToDto(eventManager);
    }

    @GetMapping(value = { "/eventManager", "/eventManager/" })
    @ResponseStatus(HttpStatus.OK)
    public List<EventManagerResponseDto> getAllEventManagers() {
        List<EventManager> eventManagers = eventManagerService.getAllEventManagers();
        return eventManagers.stream().map(this::convertToDto).toList();
    }

    // Post mapping
    @PostMapping(value = { "/eventManager", "/eventManager/" })
    @ResponseStatus(HttpStatus.CREATED)
    public EventManagerResponseDto createEventManager(@RequestBody EventManagerRequestDto eventManagerResponseDto) {
        EventManager eventManager = eventManagerService.createEventManager(
                eventManagerResponseDto.getName()
        );
        return convertToDto(eventManager);
    }

    // Put mapping
    @PutMapping(value = { "/eventManager/{id}", "/eventManager/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public EventManagerResponseDto updateEventManager(@PathVariable("id") int id,
                                                    @RequestBody EventManagerRequestDto eventManagerResponseDto) {
        EventManager eventManager = eventManagerService.updateEventManager(
                id,
                eventManagerResponseDto.getName()
        );
        return convertToDto(eventManager);
    }

    // Delete mapping
    @DeleteMapping(value = { "/eventManager/{id}", "/eventManager/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public void deleteEventManager(@PathVariable("id") int id) {
        eventManagerService.deleteEventManager(id);
    }

    /**
     * Helper method to convert EventManager objects into DTOs
     */
    private EventManagerResponseDto convertToDto(EventManager eventManager) {
        if (eventManager == null) {
            throw new EventRegistrationAppException("Event Manager not found", HttpStatus.NOT_FOUND);
        }
        return new EventManagerResponseDto(
                eventManager.getName(),
                eventManager.getGeneralUserId()
        );
    }
}
