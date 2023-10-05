package com.forcat.forcat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem{

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    /*public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문할 상품
        orderItem.setCount(count); // 주문할 수량
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count); // removeStock() 메소드를 이용하여 상품 재고 수량 감소
        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }
    // 총 가격 = 주문 가격 * 주문 수량 메소드

    public void cancel() {
        this.getItem().addStock(count);
    } // 주문 취소 시 주문 수량만큼 상품 재고 플러스*/

}