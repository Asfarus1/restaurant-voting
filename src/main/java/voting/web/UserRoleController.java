package voting.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/rest-api")
public class UserRoleController {
    private final LunchRepository repository;
    private final SecurityUtilBean securityUtilBean;

    @Value("${end-hour-for-choose-lunch}")
    private int EndHourForChooseLunch;

    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/restaurants/{restaurantId}/have-lunch")
    public ResponseEntity<?> haveLunch(@PathVariable Long restaurantId) {
        Long userId = securityUtilBean.getUserId();
        LocalDateTime now = now();
        if (now.getHour() >= EndHourForChooseLunch) {
            if (log.isDebugEnabled()) {
                securityUtilBean.getUser().ifPresent((u) ->
                        log.debug("user: {} tried to change lunch place to restaurant with id:{}",
                                u.getUsername(), restaurantId));
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(format("Lunch place can't be changed after {} am", EndHourForChooseLunch));

        }
        if (log.isDebugEnabled()) {
            securityUtilBean.getUser().ifPresent((u) ->
                    log.debug("user: {} decided to have lunch at restaurant with id:{}",
                            u.getUsername(), restaurantId));
        }
        LocalDate date = now.toLocalDate();
        repository.haveLunchIn(restaurantId, date, userId);
        return accepted().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/account", "/account/**"})
    public String forwardCurrentUser(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Long userId = securityUtilBean.getUserId();
        String forward = format("forward:{0}",
                uri.replaceAll("/account[/]?", format("/users/{0}/", userId)));
        if (log.isDebugEnabled()){
            securityUtilBean.getUser().ifPresent((u) ->
            log.debug("forwardCurrentUser() for user: {}, forward to {}",
                    u.getUsername(), forward));
        }
        return forward;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/today-menus"})
    public String forwardTodayMenus(Pageable pageable) {
        String forward = format(
                "forward:/rest-api/menus/search/date?date={0}&page={1}&size={2}&sort={3}",
                LocalDate.now(), pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        if (log.isDebugEnabled()){
            securityUtilBean.getUser().ifPresent((u) ->
                    log.debug("forwardTodayMenus() for user: {}, forward to {}",
                            u.getUsername(), forward));
        }
        return forward;
    }

    protected LocalDateTime now() {
        return LocalDateTime.now();
    }
}
