package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {//, QuerydslPredicateExecutor<Item> : 공부 필요, ItemRepositoryCustomz
    // 제네릭 타입 <엔티티 타입 클래스, 기본키 타입>

    // 쿼리 메소드 이용
    List<Item> findByItemNm(String itemNm); // 상품명으로 찾기
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail); // 상품명 또는 상품 상세 설명으로 찾기
    List<Item> findByPriceLessThan(Integer price); // 기준 가격보다 저렴한 상품 데이터 찾기
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); // 상품 가격순 내림차순으로 정렬

    // @Query 어노테이션 이용 - JPQL 작성 쿼리문
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    // 상품 상세 설명 포함된 내용 찾아서 가격순 내림차순 정렬
    /* 기존 데이터베이스 쿼리 그대로 사용하려면 nativeQuery 옵션을 사용하면 됨 -> 데이터베이스에 독립적이라는 장점을 잃게 됨
il like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDeta    @Query(value="select * from item i where i.item_detailByNative(@Param("itemDetail") String itemDetail); *//*
    */
}
