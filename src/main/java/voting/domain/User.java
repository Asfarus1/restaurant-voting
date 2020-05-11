package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
@ToString
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@NoArgsConstructor
public class User extends BaseEntity {
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;

    public User(String username, String password, Role... roles) {
        this.username = username;
        this.password = password;
        enabled = true;
        this.roles = Set.of(roles);
    }

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private List<Lunch> lunches;
}
