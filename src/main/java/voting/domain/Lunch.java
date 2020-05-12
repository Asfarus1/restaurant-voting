package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lunches", uniqueConstraints =
@UniqueConstraint(columnNames = {"date", "user_id"}))
public class Lunch extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;
}
