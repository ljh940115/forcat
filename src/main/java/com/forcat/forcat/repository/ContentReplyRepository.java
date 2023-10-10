package com.forcat.forcat.repository;

import com.forcat.forcat.entity.ContentReply;
import com.forcat.forcat.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentReplyRepository extends JpaRepository<ContentReply, Long> {

    @Query("select cr from ContentReply cr where cr.content.cno = :cno")
    Page<ContentReply> listOfContent(@Param("cno")Long cno, Pageable pageable);

    void deleteByContent_Cno(Long cno);

}
