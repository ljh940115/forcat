package com.forcat.forcat.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeListAllDTO {

    private Long noticeNo;

    private String noticeTitle;

    private String noticeWriter;

    private LocalDateTime regDate;

    private Long noticeReplyCount;

    private List<NoticeImageDTO> noticeImages;

}
