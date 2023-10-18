package com.forcat.forcat.dto.content.recent;

import com.forcat.forcat.dto.content.ContentPageRequestDTO;
import com.forcat.forcat.dto.content.recent.ContentRPageRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ContentRPageResponseDTO<E> {

    private int crpage;
    private int crsize;
    private int crtotal;
    private int crstart;//시작 페이지 번호
    private int crend;//끝 페이지 번호
    private boolean crprev;//이전 페이지의 존재 여부
    private boolean crnext;//다음 페이지의 존재 여부
    private List<E> dtoCRList;



    @Builder(builderMethodName = "withContentRAll")
    public ContentRPageResponseDTO (ContentRPageRequestDTO contentRPageRequestDTO, List<E> dtoCRList, int crtotal) {
        if (crtotal <= 0) {
            return;
        }

        //페이지 번호, 크기
        this.crpage = contentRPageRequestDTO.getCrpage ();
        this.crsize = contentRPageRequestDTO.getCrsize ();
        this.crtotal = crtotal;
        this.dtoCRList = dtoCRList;

    }

}
