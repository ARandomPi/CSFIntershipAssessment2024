package project.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.event.dto.EventManagerResponseDto;
import project.event.dto.PlannedEventRequestDto;
import project.event.dto.PlannedEventResponseDto;
import project.event.exception.EventRegistrationAppException;
import project.event.model.PlannedEvent;
import project.event.service.PlannedEventService;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@CrossOrigin("*")
@RestController
public class PlannedEventRestController {

    @Autowired
    private PlannedEventService plannedEventService;

    // Get mappings
    @GetMapping(value = { "/plannedEvent/{id}", "/plannedEvent/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public PlannedEventResponseDto getPlannedEventById(@PathVariable("id") int id) {
        PlannedEvent plannedEvent = plannedEventService.getPlannedEventById(id);
        return convertToDto(plannedEvent);
    }

    @GetMapping(value = { "/plannedEvent", "/plannedEvent/" })
    @ResponseStatus(HttpStatus.OK)
    public List<PlannedEventResponseDto> getAllPlannedEvents() {
        List<PlannedEvent> plannedEvents = plannedEventService.getAllPlannedEvents();
        return plannedEvents.stream().map(this::convertToDto).toList();
    }

    // Post mapping
    @PostMapping(value = { "/plannedEvent", "/plannedEvent/" })
    @ResponseStatus(HttpStatus.CREATED)
    public PlannedEventResponseDto createPlannedEvent(@RequestBody PlannedEventRequestDto plannedEventResponseDto) {
        GregorianCalendar date = new GregorianCalendar(
                plannedEventResponseDto.getYear(),
                plannedEventResponseDto.getMonth(),
                plannedEventResponseDto.getDay()
        );
        PlannedEvent plannedEvent = plannedEventService.createPlannedEvent(
                plannedEventResponseDto.getEventManager().getGeneralUserId(),
                plannedEventResponseDto.getEventName(),
                plannedEventResponseDto.getDescription(),
                plannedEventResponseDto.getLocation(),
                date
        );
        return convertToDto(plannedEvent);
    }

    // Put mapping
    @PutMapping(value = { "/plannedEvent/{id}", "/plannedEvent/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public PlannedEventResponseDto updatePlannedEvent(@PathVariable("id") int id,
                                                      @RequestBody PlannedEventRequestDto plannedEventResponseDto) {
        GregorianCalendar date = new GregorianCalendar(
                plannedEventResponseDto.getYear(),
                plannedEventResponseDto.getMonth(),
                plannedEventResponseDto.getDay()
        );
        PlannedEvent plannedEvent = plannedEventService.updatePlannedEvent(
                id,
                plannedEventResponseDto.getEventName(),
                plannedEventResponseDto.getDescription(),
                plannedEventResponseDto.getLocation(),
                date
        );
        return convertToDto(plannedEvent);
    }

    // Delete mapping
    @DeleteMapping(value = { "/plannedEvent/{id}", "/plannedEvent/{id}/" })
    @ResponseStatus(HttpStatus.OK)
    public void deletePlannedEvent(@PathVariable("id") int id) {
        plannedEventService.deletePlannedEvent(id);
    }

    /**
     * Helper method to convert PlannedEvent objects into DTOs
     */
    private PlannedEventResponseDto convertToDto(PlannedEvent plannedEvent) {
        if (plannedEvent == null) {
            throw new EventRegistrationAppException("Planned event not found", HttpStatus.NOT_FOUND);
        }
        EventManagerResponseDto eventManagerResponseDto = new EventManagerResponseDto(
                plannedEvent.getEventManager().getName(),
                plannedEvent.getEventManager().getGeneralUserId()
        );
        return new PlannedEventResponseDto(
                eventManagerResponseDto,
                plannedEvent.getEventName(),
                plannedEvent.getDescription(),
                plannedEvent.getLocation(),
                plannedEvent.getDate().get(Calendar.YEAR),
                plannedEvent.getDate().get(Calendar.MONTH),
                plannedEvent.getDate().get(Calendar.DAY_OF_MONTH),
                plannedEvent.getPlannedEventId()
        );
    }
}
