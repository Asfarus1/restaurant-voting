package voting.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@ToString
@Entity(name = "dishes")
public class Dish extends BaseEntity {
    @Column(nullable = false)
    private String title;
}
