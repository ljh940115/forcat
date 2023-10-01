package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    /*JPQL을 사용할 때 파라미터를 전달해야할 경우 @Param을 써야함*/
    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(@Param("bno")Long bno, Pageable pageable);

    /* void deleteByBoard_Bno(Long bno);*/
}
