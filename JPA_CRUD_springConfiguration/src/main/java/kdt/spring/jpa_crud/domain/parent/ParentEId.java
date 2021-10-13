package kdt.spring.jpa_crud.domain.parent;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
//
//- Serializable 인터페이스를 구현해야 한다.
//- eqauls, hashCode를 구현해야 한다.
//- 기본 생성자가 있어야 한다.
//- 식별자 클래스는 public 이어야 한다.
//- **@Embeddable 애노테이션이 있어야 한다.**

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ParentEId implements Serializable {
    private String id1;
    private String id2;

}
