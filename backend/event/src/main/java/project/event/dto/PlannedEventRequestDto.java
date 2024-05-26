package project.event.dto;

public class PlannedEventRequestDto {

    private EventManagerResponseDto eventManager;
    private String eventName;
    private String description;
    private String location;
    private int year;
    private int month;
    private int day;

    public PlannedEventRequestDto() {
    }

    public PlannedEventRequestDto(EventManagerResponseDto eventManager, String eventName,
                                  String description, String location, int year, int month, int day) {
        this.eventManager = eventManager;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.year = year;
        this.month = month;
        this.day = day;
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

}
