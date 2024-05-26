package project.event.dto;

public class GeneralUserRequestDto {

    private String name;

    public GeneralUserRequestDto() {
    }

    public GeneralUserRequestDto(String name) {
        this.name = name;
    }

    // Getters
    public String getName() {
        return this.name;
    }
}
