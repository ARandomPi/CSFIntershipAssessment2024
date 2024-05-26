package project.event.dto;

public class RegistrationRequestDto {

    private PlannedEventResponseDto plannedEvent;
    private GeneralUserResponseDto generalUser;

    public RegistrationRequestDto() {
    }

    public RegistrationRequestDto(PlannedEventResponseDto plannedEvent, GeneralUserResponseDto generalUser) {
        this.plannedEvent = plannedEvent;
        this.generalUser = generalUser;
    }

    // Getters
    public PlannedEventResponseDto getPlannedEvent() {
        return this.plannedEvent;
    }

    public GeneralUserResponseDto getGeneralUser() {
        return this.generalUser;
    }

}
