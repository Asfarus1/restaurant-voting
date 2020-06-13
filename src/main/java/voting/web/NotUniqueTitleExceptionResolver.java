package voting.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class NotUniqueTitleExceptionResolver implements ExceptionResolver {

    @Override
    public boolean matches(Exception ex) {
        return ex instanceof DataIntegrityViolationException && ex.getMessage().contains("(TITLE) VALUES ");
    }

    @Override
    public String getMessage(Exception ex) {
        return "Title must be unique";
    }
}
