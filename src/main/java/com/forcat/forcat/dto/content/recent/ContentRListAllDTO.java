package com.forcat.forcat.dto.content.recent;

import com.forcat.forcat.dto.content.ContentImageDTO;
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
public class ContentRListAllDTO {

    private Long cno;
    private String ctitle;
    private String cwriter;
    private LocalDateTime regDate;
    private Long creplyCount;
    private List<ContentImageDTO> contentImages;
}
