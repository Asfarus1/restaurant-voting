package voting.web;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import voting.security.exceptions.TokenAuthenticationException;

import javax.servlet.ServletException;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final List<ExceptionResolver> exceptionResolvers;

    @ExceptionHandler(TokenAuthenticationException.class)
    public ResponseEntity<?> illegalRequest(AccessDeniedException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> notFound(AccessDeniedException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> notFound(HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> sqlConstrain(DataIntegrityViolationException exception) {
        var msg = exceptionResolvers.stream()
                .filter(r -> r.matches(exception)).findFirst()
                .map(r -> r.getMessage(exception))
                .orElse(exception.getLocalizedMessage());
        return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }
}