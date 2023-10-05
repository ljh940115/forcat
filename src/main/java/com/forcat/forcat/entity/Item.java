package com.forcat.forcat.entity;

import com.forcat.forcat.constant.ItemSellStatus;
import com.forcat.forcat.dto.ItemFormDto;
import com.forcat.forcat.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item  {//extends BaseEntity
    @Id
    @Column(name="item_id")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    // -> 아예 Hibernate_sequence라는 테이블을 따로 만들어서 관리하게 된다
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob // Large Object -> CLOB, BLOB 타입으로 매핑 가능
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING) // Enum 타입 매핑(EnumType.STRING : Enum이름을 column에 저장
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regTime; // 등록 시간
    private LocalDateTime updateTime; // 수정 시간

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    /*public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber; // 상품의 재고 수량 - 주문 후 남은 재고 수량
        if(restStock < 0) { // restStock이 0보다 작을 경우 -> 주문 수량이 상품 재고 수량보다 작을 경우
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }
        this.stockNumber = restStock; // 남은 재고 수량을 현재 재고 값으로 할당
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }*/
}
