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
    @DisplayName ("상품 저장 테스트")
    public void createItemTest2 () {
        Item item = Item.builder ().itemNm ("테스트 상품1").price (10000).itemDetail ("테스트 상품 상세 설명6").itemSellStatus (ItemSellStatus.SELL).stockNumber (100).build ();
        Item savedItem = itemRepository.save (item);
        System.out.println (savedItem);
    }

    @Test
    @DisplayName ("상품명 조회 테스트")
    public void findByItemNmTest () {
        List<Item> itemList = itemRepository.findByItemNm ("테스트 상품");
        for (Item item : itemList) {
            System.out.println (item.toString ());
        }
    }

    @Test
    @DisplayName ("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest () {
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail ("테스트 상품", "테스트 상품 상세 설명5");
        for (Item item : itemList) {
            System.out.println (item.toString ());
        }
    }

    @Test
    @DisplayName ("가격 LessThan 테스트")
    public void findByPriceLessThanTest () {
        List<Item> itemList = itemRepository.findByPriceLessThan (10005);
        for (Item item : itemList) {
            System.out.println (item.toString ());
        }
    }

    @Test
    @DisplayName ("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc () {
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc (10005);
        for (Item item : itemList) {
            System.out.println (item.toString ());
        }
    }

    @Test
    @DisplayName ("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest () {
        List<Item> itemList = itemRepository.findByItemDetail ("테스트 상품 상세 설명6");
        for (Item item : itemList) {
            System.out.println (item.toString ());
        }
    }

    @Test
    @DisplayName ("Querydsl 조회 테스트1")
    public void queryDslTest () {
        JPAQueryFactory queryFactory = new JPAQueryFactory (em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom (qItem) // QueryFactory를 통해 qItem을 찾음
                .where (qItem.itemSellStatus.eq (ItemSellStatus.SELL)) //  SellStatus 가 SELL이고
                .where (qItem.itemDetail.like ("%" + "테스트 상품 상세 설명" + "%")) // 문구를 포함하고 있는
                .orderBy (qItem.price.desc ()); // 가격순으로 내림차순
        List<Item> itemList = query.fetch (); // 조회 결과를 리스트로 반환

        for (Item item : itemList) {
            System.out.println (item.toString ()); // 리스트 결과를 toString 처리하여 for문으로 출력
        }
    }
}