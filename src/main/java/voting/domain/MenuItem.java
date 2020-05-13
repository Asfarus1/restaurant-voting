package voting.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString(exclude = "menu")
@Table(name = "menu_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "dish_id"}))
public class MenuItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @DecimalMin("0")
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    //nullable=true for cascade save
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
