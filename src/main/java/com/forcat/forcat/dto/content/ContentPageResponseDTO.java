package com.forcat.forcat.dto.content;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ContentPageResponseDTO<E> {

    private int cpage;
    private int csize;
    private int ctotal;
    private int cstart;//시작 페이지 번호
    private int cend;//끝 페이지 번호
    private boolean cprev;//이전 페이지의 존재 여부
    private boolean cnext;//다음 페이지의 존재 여부
    private List<E> dtoCList;

    @Builder (builderMethodName = "withContentAll")
    public ContentPageResponseDTO (ContentPageRequestDTO contentpageRequestDTO, List<E> dtoCList, int ctotal) {
        if (ctotal <= 0) {
            return;
        }

        //페이지 번호, 크기
        this.cpage = contentpageRequestDTO.getCpage ();
        this.csize = contentpageRequestDTO.getCsize ();
        this.ctotal = ctotal;
        this.dtoCList = dtoCList;
        this.cend = (int) (Math.ceil (this.cpage / 5.0)) * 5;
        this.cstart = this.cend - 4;
        int last = (int) (Math.ceil ((ctotal / (double) csize)));
        this.cend = cend > last ? last : cend;
        this.cprev = this.cstart > 1;
        this.cnext = ctotal > this.cend * this.csize;
    }
}
