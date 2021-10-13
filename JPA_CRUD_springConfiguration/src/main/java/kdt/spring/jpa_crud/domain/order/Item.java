package kdt.spring.jpa_crud.domain.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter //lombok @
@Setter //lombok @
@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글테이블 전략
// @DiscriminatorColumn(name ="DTYPE")
public abstract class Item extends BaseEntity{ //추상클래스화
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int price;
    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItem = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setItem(this);
    }
}
