package voting.web.accessors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import voting.domain.User;
import voting.web.AccountController;
import voting.web.HaveLunchController;

@Component
public class AccountAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        EntityModel<User> model = new EntityModel<>(user);
        model.add(WebMvcLinkBuilder.linkTo(AccountController.class).withSelfRel(),
                WebMvcLinkBuilder.linkTo(HaveLunchController.class).withRel("lunch history"));
        return model;
    }
}
