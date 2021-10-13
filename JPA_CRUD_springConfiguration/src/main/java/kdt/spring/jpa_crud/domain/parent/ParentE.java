package kdt.spring.jpa_crud.domain.parent;
//EmbeddedId사용
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Getter
@Setter
public class ParentE {
    @EmbeddedId
    private ParentEId id;
}
