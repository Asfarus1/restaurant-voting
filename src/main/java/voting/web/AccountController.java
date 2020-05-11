package voting.web;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.domain.User;
import voting.repository.UserRepository;
import voting.web.accessors.AccountAssembler;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserRepository repository;
    private final AccountAssembler accessor;

    public AccountController(UserRepository repository, AccountAssembler accessor) {
        this.repository = repository;
        this.accessor = accessor;
    }

    @GetMapping
    public ResponseEntity<EntityModel<User>> get() {
        return repository.findById(SecurityUtil.getUserId())
                .map(accessor::toModel).map(ResponseEntity.ok()::body)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
