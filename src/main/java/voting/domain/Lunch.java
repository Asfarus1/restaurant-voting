package voting.domain;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
