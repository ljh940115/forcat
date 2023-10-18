package com.forcat.forcat.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class NoticePageResponseDTO<E> {

    private int noticePage;

    private int noticeSize;

    private int noticeTotal;

    private int noticeStart;

    private int noticeEnd;

    private boolean noticePrev;

    private boolean noticeNext;

    private List<E> dtoNoticeList;

    @Builder(builderMethodName = "withNoticeAll")
    public NoticePageResponseDTO(NoticePageRequestDTO noticePageRequestDTO, List<E> dtoNoticeList, int noticeTotal) {

        if(noticeTotal <= 0){

            return;
        }

        this.noticePage = noticePageRequestDTO.getNoticePage();
        this.noticeSize = noticePageRequestDTO.getNoticeSize();

        this.noticeTotal = noticeTotal;
        this.dtoNoticeList = dtoNoticeList;

        this.noticeEnd =   (int)(Math.ceil(this.noticePage / 5.0 )) *  5;
        this.noticeStart = this.noticeEnd - 4;
        int last =  (int)(Math.ceil((noticeTotal/(double)noticeSize)));
        this.noticeEnd =  noticeEnd > last ? last: noticeEnd;
        this.noticePrev = this.noticeStart > 1;
        this.noticeNext =  noticeTotal > this.noticeEnd * this.noticeSize;

    }

}
