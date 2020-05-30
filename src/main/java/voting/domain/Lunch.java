package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@AllArgsConstructor
//Variant with menu instead date and restaurant hasn't simple table unique constrains
@Table(name = "lunches", uniqueConstraints =
@UniqueConstraint(columnNames = {"date", "user_id"}))
public class Lunch extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
