package voting.domain;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity(name = "dishes")
@NoArgsConstructor
@AllArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "dishes")
public class Dish extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String title;
}
