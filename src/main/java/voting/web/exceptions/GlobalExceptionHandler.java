package voting.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import voting.security.TokenAuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenAuthenticationException.class)
    public ResponseEntity<?> illegalRequest(RuntimeException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> notFound(RuntimeException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.FORBIDDEN);
    }
}