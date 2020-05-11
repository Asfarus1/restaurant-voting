package voting.web.accessors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import voting.domain.Lunch;

import java.net.URI;

import static org.springframework.hateoas.IanaLinkRelations.SELF;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Component
public class LunchAssembler implements RepresentationModelAssembler<Lunch, EntityModel<Lunch>> {

    @Override
    public EntityModel<Lunch> toModel(Lunch lunch) {
        EntityModel<Lunch> model = new EntityModel<>(lunch);
        URI lunchUri = fromCurrentContextPath().path("/lunches/{id}").buildAndExpand(lunch.getId()).toUri();
        URI restaurantUri = fromCurrentContextPath().path("/restaurants/{id}").buildAndExpand(lunch.getRestaurant().getId()).toUri();

        model.add(new Link(lunchUri.toString(), SELF),
                new Link(restaurantUri.toString(), "restaurant"));
        return model;
    }
}
