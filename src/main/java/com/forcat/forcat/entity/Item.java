package com.forcat.forcat.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item extends BaseEntity{

    @Id
    @Column(name="item_id")
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
}
