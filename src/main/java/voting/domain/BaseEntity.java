package voting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

public class BaseEntity extends AbstractPersistable<Long> {
//
//    @Override
//    @Jso
//    public Long getId() {
//        return super.getId();
//    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return super.isNew();
    }
}
