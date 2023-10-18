package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Notice;
import com.forcat.forcat.repository.Search.SearchNoticeRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, SearchNoticeRepository {

    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select n from Notice n where n.noticeNo =:noticeNo")
    Optional<Notice> findByIdWithNoticeImages(@Param("noticeNo")Long noticeNo);

}
