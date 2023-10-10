package com.forcat.forcat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentListReplyCountDTO {

    private Long cno;
    private String ctitle;
    private String cwriter;
    private LocalDateTime regDate;
    private Long creplyCount;

} // ContentListReplyCountDTO
