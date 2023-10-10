package com.forcat.forcat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "orders")
// 정렬 키워드 order가 존재하므로 orders로 테이블 지정
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "order_id")
    private Long id;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "mid")
    private Member member;
    // 한 명의 회원이 여러 번 주문 가능 -> 주문 엔티티 기준 다대일 단방향 매핑
    private LocalDateTime orderDate; //주문일
    @Enumerated (EnumType.STRING)
    private OrderStatus orderStatus; //주문상태
    @OneToMany (mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)//, fetch = FetchType.LAZY
    private List<OrderItem> orderItems = new ArrayList<> ();

    public static Order createOrder (Member member, List<OrderItem> orderItemList) {
        Order order = new Order ();
        order.setMember (member);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem (orderItem);
        }
        order.setOrderStatus (OrderStatus.ORDER);
        order.setOrderDate (LocalDateTime.now ());
        return order;
    }

    public void addOrderItem (OrderItem orderItem) {
        orderItems.add (orderItem);
        orderItem.setOrder (this);
    }

    public int getTotalPrice () {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice ();
        }
        return totalPrice;
    }

    public void cancelOrder () {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel ();
        }
    }
}