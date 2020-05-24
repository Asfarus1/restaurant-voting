package voting.web;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.repository.LunchRepository;
import voting.security.SecurityUtil;

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

    @PutMapping(value = "/restaurants/{restaurantId}/have-lunch", consumes = "application/json")
    public ResponseEntity<?> haveLunch(/*@AuthenticationPrincipal org.springframework.security.core.userdetails.User authPrincipal, */@PathVariable Long restaurantId) {
        Long userId = SecurityUtil.getUserId();
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

    @GetMapping("/self")
    public String getLunchHistory(HttpServletRequest request) {
        String s = request.getRequestURI();
        System.out.println("===========" + s);
        String red = "forward:" + s.replaceAll("/self[/]?", "/users/" + SecurityUtil.getUserId());
        System.out.println(red);
        return red;
    }

    @Lookup
    protected abstract LocalDateTime now();
}
