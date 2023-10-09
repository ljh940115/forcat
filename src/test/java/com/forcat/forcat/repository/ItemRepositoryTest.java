package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.ItemSellStatus;
import com.forcat.forcat.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    @PersistenceContext // 영속성 컨텍스트 -> EntityManager를 빈으로 주입할 때 사용
    EntityManager em; // EntityManager -> Entity CRUD를 위한 메소드 제공

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest2() {
        Item item = Item.builder()
                .itemNm("테스트 상품1")
                .price(10000)
                .itemDetail("테스트 상품 상세 설명6")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품");
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품", "테스트 상품 상세 설명5");
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명6");
        for(Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem) // QueryFactory를 통해 qItem을 찾음
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL)) //  SellStatus 가 SELL이고
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%")) // 문구를 포함하고 있는
                .orderBy(qItem.price.desc()); // 가격순으로 내림차순
        List<Item> itemList = query.fetch(); // 조회 결과를 리스트로 반환

        for(Item item : itemList) {
            System.out.println(item.toString()); // 리스트 결과를 toString 처리하여 for문으로 출력
        }
    }

/*    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // 동적쿼리 where문 담는 컨테이너
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%")); // '테스트 상품 상세 설명' 문구를 포함하고 있는
        booleanBuilder.and(item.price.gt(price)); // 10003보다 비싼

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) { // StringUtils.equals(A, B) : A와 B의 문자열이 같은지
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL)); // 상품 판매 상태가 SELL인
        }

        Pageable pageable = PageRequest.of(0, 5); // 0번째 페이지부터 5개 항목 페이징
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable); // 조건 결합하여 결과를 itemPagingResult에 저장
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem :  resultItemList) {
            System.out.println(resultItem.toString());
        }
    }*/
}