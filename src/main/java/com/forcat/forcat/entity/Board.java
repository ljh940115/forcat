package com.forcat.forcat.entity;

import lombok.*;

import javax.persistence.*;

//엔티티 클래스 필수 사항 @Entity, @Id
//실제 DB와 매칭할 클래스
@Entity//엔티티 클래스 명시
@Getter
@Builder//빌더 클래스 명시
@AllArgsConstructor//모든 필드값을 파라미터로 받는 생성자로 만듦
@NoArgsConstructor//값 없는 기본 생성자 생성
@ToString//객체 값을 문자열로 리턴
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//키 생성 전략, 자동 키 생성.DB에 위임
    private Long bno;//게시글 일련번호
    @Column(length = 500, nullable = false)//데이터 길이, null 허용 X
    private String title;//게시글 제목
    @Column(length = 2000, nullable = false)//데이터 길이, null 허용 X
    private String content;//게시글 내용
    @Column(length = 50, nullable = false)//데이터 길이, null 허용 X
    private String writer;//게시글 작성자

    //update는 등록 시간이 필요하므로 change() 메서드 생성
    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }
}
