package com.forcat.forcat.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeImageDTO {

    private String uuid;

    private String fileName;

    private int ord;

}
