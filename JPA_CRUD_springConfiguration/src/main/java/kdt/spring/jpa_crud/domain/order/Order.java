package kdt.spring.jpa_crud.domain.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter //lombok @
@Setter //lombok @
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    @Id
    @Column(name = "id")
    private String uuid;
    @Column(name = "memo")
    private String memo;
    @Enumerated(value = EnumType.STRING) // 값이 들어가기 위한 STRING
    private OrderStatus orderStatus;
    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDatetime;

    // member_fk
    @Column(name = "member_id", insertable = false,updatable = false)
    private Long memberId;
    //order.setMemberId() 이런걸 해도 (엔티티가 펄시스트가 되어도 쿼리가 실제 날아가지 않게됨)

    // 다대일? 주문 N : 1 멤버일수도있음
    @ManyToOne (fetch = FetchType.EAGER) //fetch타입에 따라 언제 가져올지 정할수있음
    @JoinColumn(name="member_id",referencedColumnName = "id")
    private Member memberG;// 연관관계 단방향 맵핑 (회원에서 멤버)


    // 연관관계 편의 메소드 즉 member.set을 하지 않아도 자동으로 되게
    // 놓칠수 있는 부분을 막아줌
    public void setMember(Member member){
        if(Objects.nonNull(this.memberG)){ //이미 있으면 지우고
            member.getOrders().remove(this);
        }
        this.memberG = member;
        member.getOrders().add(this);
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // 커밋되는 순간 준영속 상태에서 영속상태로 바뀜
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
    }
}
