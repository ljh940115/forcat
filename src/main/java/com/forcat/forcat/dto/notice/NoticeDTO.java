package com.forcat.forcat.dto.notice;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    private Long noticeNo;

    @NotEmpty
    @Size(min=3, max = 100)
    private String noticeTitle;

    @NotEmpty
    private String noticeContent;

    @NotEmpty
    private String noticeWriter;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    //첨부파일의 이름들
    private List<String> fileNames;

}
