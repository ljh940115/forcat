package com.forcat.forcat.dto.content;

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
public class ContentDTO {

    private Long cno;

    @NotEmpty
    @Size(min=3, max = 100)
    private String ctitle;
    @NotEmpty
    private String ccontent;
    @NotEmpty
    private String cwriter;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    //첨부파일의 이름들
    private List<String> fileNames;

} // ContentDTO
