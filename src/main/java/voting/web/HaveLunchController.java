package voting.web;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import voting.domain.Lunch;
import voting.repository.LunchRepository;
import voting.repository.RestaurantRepository;
import voting.repository.UserRepository;
import voting.web.accessors.LunchAssembler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.accepted;

@RepositoryRestController
public abstract class HaveLunchController {
    private final LunchRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final LunchAssembler assembler;

    public HaveLunchController(LunchRepository repository, RestaurantRepository restaurantRepository, UserRepository userRepository, LunchAssembler assembler) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @PutMapping("restaurants/{restaurantId}/have-lunch")
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

    @GetMapping("/")
    public ResponseEntity<?> getLunchHistory(Pageable pageable, PagedResourcesAssembler<Lunch> pagedResourcesAssembler) {
        Page<Lunch> page = repository.findAll(pageable);
        return ResponseEntity.ok().body(pagedResourcesAssembler.toModel(page, assembler));
    }

    @Lookup
    protected abstract LocalDateTime now();
}
