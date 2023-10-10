package com.forcat.forcat.security.dto;

import com.forcat.forcat.dto.content.ContentPageRequestDTO;
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

    //시작 페이지 번호
    private int cstart;
    //끝 페이지 번호
    private int cend;

    //이전 페이지의 존재 여부
    private boolean cprev;
    //다음 페이지의 존재 여부
    private boolean cnext;

    private List<E> dtoCList;

    @Builder(builderMethodName = "withContentAll")
    public ContentPageResponseDTO(ContentPageRequestDTO contentpageRequestDTO, List<E> dtoCList, int ctotal){

        if(ctotal <= 0){
            return;
        }

        //페이지 번호, 크기
        this.cpage = contentpageRequestDTO.getCpage();
        this.csize = contentpageRequestDTO.getCsize();

        this.ctotal = ctotal;
        this.dtoCList = dtoCList;

        this.cend =   (int)(Math.ceil(this.cpage / 10.0 )) *  10;
        this.cstart = this.cend - 9;
        int clast =  (int)(Math.ceil((ctotal/(double)csize)));
        this.cend =  cend > clast ? clast: cend;
        this.cprev = this.cstart > 1;
        this.cnext =  ctotal > this.cend * this.csize;

    }



} // ContentPageResponseDTO
