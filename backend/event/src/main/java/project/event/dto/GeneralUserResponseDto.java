package project.event.dto;

public class GeneralUserResponseDto {

    private int generalUserId;
    private String name;

    public GeneralUserResponseDto() {
    }

    public GeneralUserResponseDto(String name, int generalUserId) {
        this.name = name;
        this.generalUserId = generalUserId;
    }

    // Getters
    public String getName() {
        return this.name;
    }

    public int getGeneralUserId() {
        return this.generalUserId;
    }
}
