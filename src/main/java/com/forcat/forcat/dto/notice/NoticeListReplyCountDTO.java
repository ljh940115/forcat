package com.forcat.forcat.dto.notice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeListReplyCountDTO {

    private Long noticeNo;

    private String noticeTitle;

    private String noticeWriter;

    private LocalDateTime regDate;

    private Long noticeReplyCount;

}
