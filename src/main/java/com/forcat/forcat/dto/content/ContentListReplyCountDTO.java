package com.forcat.forcat.dto.content;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentListReplyCountDTO {

    private Long cno;
    private String ctitle;
    private String cwriter;
    private LocalDateTime regDate;
    private Long creplyCount;
}
