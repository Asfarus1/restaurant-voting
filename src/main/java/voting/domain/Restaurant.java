package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity(name = "restaurants")
@Getter
@Setter
@ToString(exclude = {"lunches", "menus"})
@NoArgsConstructor
//https://stackoverflow.com/questions/52656517/no-serializer-found-for-class-org-hibernate-proxy-pojo-bytebuddy-bytebuddyinterc
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Restaurant extends BaseEntity {

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant")
    private List<Lunch> lunches;

    public Restaurant(String title) {
        this.title = title;
    }
}
