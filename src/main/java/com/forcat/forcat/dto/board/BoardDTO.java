package com.forcat.forcat.dto.board;

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
public class BoardDTO {//엔티티와 비슷한 역할을 하나 일회성, View와 통신

   private Long bno;
   private String mid;
   @NotEmpty
   @Size(min=3, max = 100)
   private String title;
   @NotEmpty
   private String content;
   @NotEmpty
   private String writer;
   private LocalDateTime regDate;
   private LocalDateTime modDate;
   //첨부파일의 이름들
   private List<String> fileNames;
}
