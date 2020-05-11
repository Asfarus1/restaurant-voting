package voting.web;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import voting.domain.Lunch;
import voting.domain.Restaurant;
import voting.domain.User;
import voting.repository.LunchRepository;
import voting.repository.RestaurantRepository;
import voting.repository.UserRepository;
import voting.web.accessors.LunchAssembler;
import voting.web.exceptions.NotFoundException;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping("/lunch")
public class LunchController {
    private final LunchRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final LunchAssembler assembler;

    public LunchController(LunchRepository repository, RestaurantRepository restaurantRepository, UserRepository userRepository, LunchAssembler assembler) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<?> chooseRestaurant(@RequestParam Long restaurantId) {
        LocalDateTime now = now();
        if (now.getHour() > 10) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Lunch place can't be changed after 11 am");
        }

        LocalDate date = now.toLocalDate();
        Long id = saveLunch(restaurantId, date);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return accepted().location(uri).build();
    }

    @GetMapping
    public ResponseEntity<?> getLunchHistory(Pageable pageable, PagedResourcesAssembler<Lunch> pagedResourcesAssembler){
        Page<Lunch> page = repository.findAll(pageable);
        return ResponseEntity.ok().body(pagedResourcesAssembler.toModel(page,assembler));
    }

    @Lookup
    private LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Transactional
    protected Long saveLunch(Long restaurantId, LocalDate date) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
        User user = userRepository.findById(SecurityUtil.getUserId()).orElseThrow();

        Lunch lunch = repository.findByUserAndDate(user, date).orElse(new Lunch(user, date));
        lunch.setRestaurant(restaurant);
        repository.save(lunch);
        return Objects.requireNonNull(lunch.getId());
    }
}
