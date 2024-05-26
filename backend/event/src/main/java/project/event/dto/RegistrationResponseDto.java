package project.event.dto;

public class RegistrationResponseDto {

    private int registrationId;
    private PlannedEventResponseDto plannedEvent;
    private GeneralUserResponseDto generalUser;

    public RegistrationResponseDto() {
    }

    public RegistrationResponseDto(PlannedEventResponseDto plannedEvent,
                                   GeneralUserResponseDto generalUser, int registrationId) {
        this.plannedEvent = plannedEvent;
        this.generalUser = generalUser;
        this.registrationId = registrationId;
    }

    // Getters
    public PlannedEventResponseDto getPlannedEvent() {
        return this.plannedEvent;
    }

    public GeneralUserResponseDto getGeneralUser() {
        return this.generalUser;
    }

    public int getRegistrationId() {
        return this.registrationId;
    }

}
