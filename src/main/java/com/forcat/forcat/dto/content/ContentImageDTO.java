package com.forcat.forcat.dto.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentImageDTO {

    private String uuid;

    private String fileName;

    private int ord;

} // ContentImageDTO
