package com.forcat.forcat.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity//엔티티 클래스 명시
@Getter
@Builder//빌더 클래스 명시
@AllArgsConstructor//모든 필드값을 파라미터로 받는 생성자로 만듦
@NoArgsConstructor//값 없는 기본 생성자 생성
@ToString(exclude = "imageSet")//객체 값을 문자열로 리턴
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//키 생성 전략, 자동 키 생성.DB에 위임
    private Long noticeNo;//게시글 일련번호

    @Column(length = 500, nullable = false)//데이터 길이, null 허용 X
    private String noticeTitle;//게시글 제목

    @Column(length = 2000, nullable = false)//데이터 길이, null 허용 X
    private String noticeContent;//게시글 내용

    @Column(length = 50, nullable = false)//데이터 길이, null 허용 X
    private String noticeWriter;//게시글 작성자

    @ManyToOne(fetch = FetchType.LAZY) // 다대일(Many-to-One) 관계 설정
    @JoinColumn(name = "mid") // 멤버 엔티티의 기본 키(mid)를 외래 키로 사용
    private Member member; // 보드가 어떤 멤버에게 속하는지를 나타내는 필드

    public void change(String noticeTitle, String noticeContent){//update는 등록 시간이 필요하므로 change() 메서드 생성
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
    }

    @OneToMany(mappedBy = "notice", //BoardImage의 Board 변수
            cascade = {CascadeType.ALL}, //영속성 전이 작업을 활성화
            fetch = FetchType.LAZY, //지연 로딩
            orphanRemoval = true) //부모 엔티티 삭제와 동시에 자식 엔티티 삭제 처리
    @Builder.Default //빈 HashSet으로 초기화
    @BatchSize(size=20)//지정된 수만큼 조회할 때 한번에 in 조건 사용
    //Board 엔티티와 연관된 BoardImage 엔티티의 집합
    private Set<NoticeImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){//Board 엔티티에 이미지를 추가
        //BoardImage 엔티티를 생성하고, UUID와 파일 이름을 설정한 다음
        //현재 Board 엔티티와 연결, 다음 이미지 세트(imageSet)에 새 이미지를 추가
        NoticeImage noticeImage = NoticeImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .notice(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(noticeImage);
    }

    public void clearImages() {//Board 엔티티와 연결된 모든 이미지를 제거하는 데 사용
        //imageSet의 각 BoardImage 엔티티에 대해 changeBoard(null)을 호출, 해당 이미지의 board 속성을 제거
        imageSet.forEach(noticeImage -> noticeImage.changeNotice(null));

        this.imageSet.clear();//imageSet을 비움
    }


}
