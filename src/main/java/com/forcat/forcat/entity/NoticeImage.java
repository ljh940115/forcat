package com.forcat.forcat.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "notice")
public class NoticeImage implements Comparable<NoticeImage> {

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Notice notice;

    @Override
    public int compareTo(NoticeImage other) {
        return this.ord - other.ord;
    }//BoardImage의 board 필드를 변경하는 메서드

    public void changeNotice(Notice notice){
        this.notice = notice;
    }//이미지들을 정렬하는 메서드


}
