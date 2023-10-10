package com.forcat.forcat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "order_item_id")
    private Long id;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "item_id")
    private Item item;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "order_id")
    private Order order;
    private int orderPrice; //주문가격
    private int count; //수량

    public static OrderItem createOrderItem (Item item, int count) {
        OrderItem orderItem = new OrderItem ();
        orderItem.setItem (item);//주문할 상품 저장
        orderItem.setCount (count);//주문할 개수 저장
        orderItem.setOrderPrice (item.getPrice ());//아이템의 현재 가격 저장
        item.removeStock (count);////주문 수량만큼 상품 재고 삭제
        item.changeStatus();
        return orderItem;
    }

    public int getTotalPrice () {
        return orderPrice * count;
    }//주문 가격 * 주문 개수를 곱해 총 가격 계산

    public void cancel () {
        this.getItem ().addStock (count);
        this.getItem().changeStatus();
    }//주문 취소 시 재고 증가
}