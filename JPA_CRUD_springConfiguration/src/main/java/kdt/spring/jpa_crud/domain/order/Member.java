package kdt.spring.jpa_crud.domain.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Setter
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // 시퀀스 전략
    private Long id;
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    @Column(name = "nick_name", nullable =false, length = 30, unique = true)
    private String nickName;
    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "address",nullable = false)
    private String address;
    @Column(name ="description")
    private String description;

    //회원에서 주문을 확인해야함
    //회원은 여러개의 주문을 가질 수 있으므로 컬렉션타입으로 선언
    @OneToMany(mappedBy = "memberG" )//연관관계 주인 ""에 오더의 member와 변수가 맞아야함
    private List<Order> orders = new ArrayList<>();

    // 연관관계 편의 메소드
    public void addOrder(Order order){
        order.setMember(this);
    }
}
