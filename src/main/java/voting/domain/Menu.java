package voting.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}))
public class Menu extends BaseEntity {

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id")
    private List<MenuItem> items;
}
