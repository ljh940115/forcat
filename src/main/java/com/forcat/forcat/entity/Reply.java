package com.forcat.forcat.entity;

import lombok.*;

import javax.persistence.*;


@Entity//엔티티 클래스
@Table (name = "Reply", indexes = {@Index (name = "idx_reply_board_bno", columnList = "board_bno")})//엔티티 클래스가 매핑되는 데이터베이스 테이블의 정보를 지정
@Getter//게터 자동 생성
@Builder//빌버 패턴 사용
@AllArgsConstructor//인자 있는 생성자 자동 생성
@NoArgsConstructor//인자 없는 생성자 자동 생성
@ToString//(exclude = "board")//ToString 자동 생성, board 필드 제외 *참조 객체 반드시 제외
//@ToString
public class Reply extends BaseEntity {

    @Id//PK
    @GeneratedValue (strategy = GenerationType.IDENTITY)//번호 자동 생성
    private Long rno;
    @ManyToOne (fetch = FetchType.LAZY)//다대일 관계
    private Board board;
    private String replyText;
    private String replyer;

    public void changeText (String text) {//댓글 수정하는 경우 replyText만 수정할 수 있으므로 추가
        this.replyText = text;
    }//댓글 내용을 수정하는 메서드
}


