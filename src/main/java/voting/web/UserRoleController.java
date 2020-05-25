package voting.web;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import voting.repository.LunchRepository;
import voting.security.SecurityUtilBean;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.accepted;

@Controller
public abstract class UserRoleController {
    private final LunchRepository repository;

    protected UserRoleController(LunchRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('USER)')")
    @PutMapping(value = "/restaurants/{restaurantId}/have-lunch", consumes = "application/json")
    public ResponseEntity<?> haveLunch(@PathVariable Long restaurantId) {
        Long userId = SecurityUtilBean.getUserId();
        System.out.println("have-lunch:" + userId);
        LocalDateTime now = now();
        if (now.getHour() > 10) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Lunch place can't be changed after 11 am");
        }
        LocalDate date = now.toLocalDate();
        repository.haveLunchIn(restaurantId, date, userId);
        return accepted().build();
    }

    @PreAuthorize("hasRole('USER)')")
    @GetMapping("/account")
    public String forwardCurrentUser(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return "forward:" +
                uri.replaceAll("/account[/]?", "/users/" + SecurityUtilBean.getUserId());
    }

    @Lookup
    protected abstract LocalDateTime now();
}
