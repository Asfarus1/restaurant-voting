package voting.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import voting.repository.LunchRepository;
import voting.security.SecurityUtilBean;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.text.MessageFormat.format;
import static org.springframework.http.ResponseEntity.accepted;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rest-api")
public class UserRoleController {
    private final LunchRepository repository;
    private final SecurityUtilBean securityUtilBean;

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping(value = "/restaurants/{restaurantId}/have-lunch")
    public ResponseEntity<?> haveLunch(@PathVariable Long restaurantId) {
        Long userId = securityUtilBean.getUserId();
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/account", "/account/**"})
    public String forwardCurrentUser(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return format("forward:{0}",
                uri.replaceAll("/account[/]?", format("/users/{0}/", securityUtilBean.getUserId())));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/today-menus"})
    public String forwardTodayMenus(Pageable pageable, HttpServletRequest request) {
        request.setAttribute("date", LocalDate.now());
        request.setAttribute("page", pageable.getPageNumber());
        request.setAttribute("size", pageable.getPageSize());
        request.setAttribute("sort", pageable.getSort());

        return format("/menus/search/date");
    }

    protected LocalDateTime now() {
        return LocalDateTime.now();
    }
}
