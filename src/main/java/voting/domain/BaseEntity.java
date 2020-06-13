package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity extends AbstractPersistable<Long> {

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    @Getter
    private Date updated;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return super.isNew();
    }

    @PrePersist
    protected void onCreate() {
        updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}
