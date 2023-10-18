
package com.forcat.forcat.repository.Search;


import com.forcat.forcat.dto.notice.NoticeListAllDTO;
import com.forcat.forcat.dto.notice.NoticeListReplyCountDTO;
import com.forcat.forcat.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchNoticeRepository {

    Page<Notice> searchNotice1(Pageable pageable);

    //Querydsl로 검색 처리
    Page<Notice> searchNoticeAll(String[] types, String keyword, Pageable pageable);

    Page<NoticeListReplyCountDTO> searchWithNoticeReplyCount(String[] types, String keyword, Pageable pageable);

    Page<NoticeListAllDTO> searchWithNoticeAll(String[] types, String keyword, Pageable pageable);


}
