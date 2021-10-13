package kdt.spring.jpa_crud.domain.parent;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//- Serializable 인터페이스를 구현해야 한다.
//- eqauls, hashCode를 구현해야 한다. (영속성 컨텍스트에서 키값 구분을 위해서)
//- 기본 생성자가 있어야 한다.
//- 식별자 클래스는 public 이어야 한다.

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ParentId implements Serializable {
    private String id1;
    private String id2;
}
