package com.forcat.forcat.repository;

import com.forcat.forcat.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    // 쿼리메소드 -> Id를 기준으로 오름차순 정려하여 ItemId를 찾음
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}
