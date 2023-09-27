package com.forcat.forcat.security.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {//엔티티와 비슷한 역할을 하나 일회성, View와 통신

   private Long bno;

   @NotEmpty
   @Size(min=3, max = 100)
   private String title;

   @NotEmpty
   private String content;

   @NotEmpty
   private String writer;

   private LocalDateTime regDate;
   private LocalDateTime modDate;
}
