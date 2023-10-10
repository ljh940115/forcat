package com.forcat.forcat.repository;

import com.forcat.forcat.dto.content.ContentListReplyCountDTO;
import com.forcat.forcat.entity.Content;
import com.forcat.forcat.dto.content.ContentListAllDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchContentRepository {

    Page<Content> searchContent1 (Pageable pageable);

    //Querydsl로 검색 처리
    Page<Content> searchContentAll (String[] types, String keyword, Pageable pageable);

    Page<ContentListReplyCountDTO> searchWithContentReplyCount (String[] types, String keyword, Pageable pageable);

    Page<ContentListAllDTO> searchWithContentAll (String[] types, String keyword, Pageable pageable);

    Page<ContentListAllDTO> searchWithContentRecent (String[] types, String keyword, Pageable pageable);
}
