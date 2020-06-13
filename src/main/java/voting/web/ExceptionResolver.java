package voting.web;

public interface ExceptionResolver {

    boolean matches(Exception ex);

    String getMessage(Exception ex);
}
