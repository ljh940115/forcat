package com.forcat.forcat.entity;

import com.forcat.forcat.dto.shop.ItemFormDto;
import com.forcat.forcat.exception.OutOfStockException;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString (exclude = "imageSet")//객체 값을 문자열로 리턴
public class Item extends BaseEntity {

    @Id
    @Column (name = "item_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; // 상품 코드
    @Column (nullable = false, length = 50)
    private String itemNm; // 상품명
    @Column (name = "price", nullable = false)
    private int price; // 가격
    @Column (nullable = false)
    private int stockNumber; // 재고 수량
    @Lob // Large Object -> CLOB, BLOB 타입으로 매핑 가능
    @Column (nullable = false)
    private String itemDetail; // 상품 상세 설명
    @Enumerated (EnumType.STRING) // Enum 타입 매핑(EnumType.STRING : Enum이름을 column에 저장
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    @OneToMany (mappedBy = "item",//BoardImage의 Board 변수
            cascade = {CascadeType.ALL},//영속성 전이 작업을 활성화
            fetch = FetchType.LAZY,//지연 로딩
            orphanRemoval = true)//부모 엔티티 삭제와 동시에 자식 엔티티 삭제 처리
    @Builder.Default//빈 HashSet으로 초기화
    @BatchSize (size = 20)//지정된 수만큼 조회할 때 한번에 in 조건 사용
    //Board 엔티티와 연관된 BoardImage 엔티티의 집합
    private Set<ItemImg> imageSet = new HashSet<> ();

    public void updateItem (ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm ();
        this.price = itemFormDto.getPrice ();
        this.stockNumber = itemFormDto.getStockNumber ();
        this.itemDetail = itemFormDto.getItemDetail ();
        this.itemSellStatus = itemFormDto.getItemSellStatus ();
    }

    public void removeStock (int stockNumber) {//상품 주문 시 재고 수량 변경
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException ("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock (int stockNumber) {//주문 취소 시 재고 증가
        this.stockNumber += stockNumber;
    }
}
