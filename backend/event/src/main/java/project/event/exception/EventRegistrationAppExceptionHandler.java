package project.event.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EventRegistrationAppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EventRegistrationAppException.class)
    public ResponseEntity<String> handleEventRegistrationAppException(EventRegistrationAppException e) {
        return new ResponseEntity<String>(e.getMessage(), e.getStatus());
    }
}
