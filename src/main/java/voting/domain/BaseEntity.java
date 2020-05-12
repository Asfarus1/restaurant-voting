package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

public abstract class BaseEntity extends AbstractPersistable<Long> {

    @JsonIgnore
    @Override
    public boolean isNew() {
        return super.isNew();
    }
}
