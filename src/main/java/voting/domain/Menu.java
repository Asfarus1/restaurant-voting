package voting.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}))
@NamedQueries({
        @NamedQuery(name = Menu.BY_DATE_AND_RESTAURANT,
                query = "SELECT m FROM Menu m WHERE m.date=?1 AND m.restaurant=?2"),
        //For most popular operation create
        // with CascadeType.PERSIST calls insert and update queries,
        @NamedQuery(name = Menu.REMOVE_ITEMS,
                query = "DELETE FROM MenuItem i WHERE i.menu.id=?1")})
@NamedEntityGraph(name = Menu.WITH_ITEMS,
        attributeNodes = @NamedAttributeNode("items"),
        subgraphs = @NamedSubgraph(name = "items", attributeNodes = @NamedAttributeNode("dish")))
public class Menu extends BaseEntity {

    public static final String BY_DATE_AND_RESTAURANT = "Menu.getByDateAndRestaurant";
    public static final String REMOVE_ITEMS = "Menu.removeItems";
    public static final String WITH_ITEMS = "Menu.withItems";

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotEmpty
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 200)
    private List<MenuItem> items;
}
