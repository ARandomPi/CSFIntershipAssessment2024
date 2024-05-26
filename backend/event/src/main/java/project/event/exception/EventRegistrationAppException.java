package project.event.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class EventRegistrationAppException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final HttpStatus status;

    public EventRegistrationAppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // Getter
    public HttpStatus getStatus() {
        return status;
    }
}
