package com.forcat.forcat.entity;

import com.forcat.forcat.dto.ItemFormDto;
import com.forcat.forcat.exception.OutOfStockException;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")//객체 값을 문자열로 리턴
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

    @OneToMany(mappedBy = "item",//BoardImage의 Board 변수
            cascade = {CascadeType.ALL},//영속성 전이 작업을 활성화
            fetch = FetchType.LAZY,//지연 로딩
            orphanRemoval = true)//부모 엔티티 삭제와 동시에 자식 엔티티 삭제 처리
    @Builder.Default//빈 HashSet으로 초기화
    @BatchSize(size=20)//지정된 수만큼 조회할 때 한번에 in 조건 사용
    //Board 엔티티와 연관된 BoardImage 엔티티의 집합
    private Set<ItemImg> imageSet = new HashSet<>();

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }
/*    public void addImage(String imgName, String oriImgName){//Board 엔티티에 이미지를 추가
        //BoardImage 엔티티를 생성하고, UUID와 파일 이름을 설정한 다음
        //현재 Board 엔티티와 연결, 다음 이미지 세트(imageSet)에 새 이미지를 추가
        ItemImg itemImg = ItemImg.builder()
                .imgName()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages() {//Board 엔티티와 연결된 모든 이미지를 제거하는 데 사용
        //imageSet의 각 BoardImage 엔티티에 대해 changeBoard(null)을 호출, 해당 이미지의 board 속성을 제거
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();//imageSet을 비움
    }*/
}
