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
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {//Comparable는 OneToMany 처리에서 순번에 맞게 정렬

    @Id
    private String uuid;
    private String fileName;
    private int ord;
    @ManyToOne
    private Board board;
    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }//BoardImage의 board 필드를 변경하는 메서드
    public void changeBoard(Board board){
        this.board = board;
    }//이미지들을 정렬하는 메서드

}
