package com.forcat.forcat.repository;

import com.forcat.forcat.entity.NoticeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {

    @Query("select nr from NoticeReply nr where nr.notice.noticeNo = :noticeNo")
    Page<NoticeReply> listOfNotice(@Param("noticeNo")Long noticeNo, Pageable pageable);

    void deleteByNotice_NoticeNo(Long noticeNo);


}
