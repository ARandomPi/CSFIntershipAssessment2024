package project.event.dto;

public class PlannedEventResponseDto {

    private int eventId;
    private EventManagerResponseDto eventManager;
    private String eventName;
    private String description;
    private String location;
    private int year;
    private int month;
    private int day;

    public PlannedEventResponseDto() {
    }

    public PlannedEventResponseDto(EventManagerResponseDto eventManager, String eventName,
                                   String description, String location, int year, int month, int day, int eventId) {
        this.eventManager = eventManager;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.year = year;
        this.month = month;
        this.day = day;
        this.eventId = eventId;
    }

    // Getters
    public EventManagerResponseDto getEventManager() {
        return this.eventManager;
    }

    public String getEventName() {
        return this.eventName;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getEventId() {
        return this.eventId;
    }

}
