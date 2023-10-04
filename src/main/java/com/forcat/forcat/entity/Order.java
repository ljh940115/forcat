package com.forcat.forcat.entity;

import com.forcat.forcat.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
// 정렬 키워드 order가 존재하므로 orders로 테이블 지정
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    // 한 명의 회원이 여러 번 주문 가능 -> 주문 엔티티 기준 다대일 단방향 매핑

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
                orphanRemoval = true, fetch = FetchType.LAZY) // 일대다 매핑
    /* 연관관계 주인은 OrderItem
    Order가 주인이 아니므로 mappedBy 속성으로 주인 설정(OrderItem의 Order) */
    /* CascadeType.All : 부모 엔티티 영속성 상태 변화를 자식 엔티티에 모두 전이 */
    /* orphanRemoval = true -> 연관 관계가 끊어진 자식 엔티티(고아 객체)를 자동으로 제거 */
    private List<OrderItem> orderItems = new ArrayList<>();
    // 하나의 주문이 여러개의 주문 상품을 갖게 되니까 List 자료형 사용

    // private LocalDateTime regTime;
    // private LocalDateTime updateTime;

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); // 주문 상품 정보 담기 -> Order 객체의 orderItems에 OrderItem 객체를 추가
        orderItem.setOrder(this); // OrderItem 객체에 order 객체 세팅
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        // 장바구니에 여러 개의 상품 주문 가능하도록 List 형태로 파라미터 값 받기
        Order order = new Order();
        order.setMember(member); // 상품 주문한 회원 정보 세팅
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem); // Order 객체에 orderItem 객체 추가
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now()); // 주문 시간을 현재 시간으로 설정
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    } // 총 주문 금액 계산 메소드

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}