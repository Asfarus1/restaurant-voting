package voting.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity(name = "restaurants")
@Getter
@Setter
@ToString
public class Restaurant extends BaseEntity {

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private List<Menu> menus;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private List<Lunch> lunches;
}
