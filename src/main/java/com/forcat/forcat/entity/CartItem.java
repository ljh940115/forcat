package com.forcat.forcat.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table (name = "cart_item")
public class CartItem extends BaseEntity {//장바구니에 담을 상품

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "cart_item_id")
    private Long id;
    @ManyToOne (fetch = FetchType.LAZY) // 다대일 매핑
    @JoinColumn (name = "cart_id")
    private Cart cart;
    @ManyToOne (fetch = FetchType.LAZY) // 다대일 매핑
    @JoinColumn (name = "item_id")
    private Item item;
    private int count;

    public static CartItem createCartItem (Cart cart, Item item, int count) {//CartItem 객체를 생성
        CartItem cartItem = new CartItem ();
        cartItem.setCart (cart);
        cartItem.setItem (item);
        cartItem.setCount (count);
        return cartItem;
    }

    //장바구니 수량 증가 메소드
    public void addCount (int count) {
        this.count += count;
    }//CartItem의 count 필드에 더하는 역할

    public void updateCount (int count) {
        this.count = count;
    }//CartItem의 count 필드를 갱신하는 역할
}
