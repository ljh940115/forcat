package com.forcat.forcat.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString (exclude = "imageSet")
//@Transactional
public class Content extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)//키 생성 전략, 자동 키 생성.DB에 위임
    private Long cno; //게시글 일련번호

    @Column (length = 500, nullable = false)//데이터 길이, null 허용 X
    private String ctitle; // content 제목

    @Column (length = 2000, nullable = false)//데이터 길이, null 허용 X
    private String ccontent; // content 내용

    @Column (length = 50, nullable = false)//데이터 길이, null 허용 X
    private String cwriter; // content 작성자
    @OneToMany (mappedBy = "content", // BoardImage의 Board 변수
            cascade = {CascadeType.ALL}, // 영속성 전이 작업을 활성화
            fetch = FetchType.LAZY, // 지연 로딩
            orphanRemoval = true) // 부모 엔티티 삭제와 동시에 자식 엔티티 삭제 처리
    @Builder.Default // 빈 HashSet으로 초기화
    @BatchSize (size = 20)//지정된 수만큼 조회할 때 한번에 in 조건 사용
    //Board 엔티티와 연관된 BoardImage 엔티티의 집합
    private Set<ContentImage> imageSet = new HashSet<> ();

    public void change (String ctitle, String ccontent) {
        this.ctitle = ctitle;
        this.ccontent = ccontent;
    } // change

    public void addImage (String uuid, String fileName) { // Board 엔티티에 이미지를 추가
        //BoardImage 엔티티를 생성하고, UUID와 파일 이름을 설정한 다음
        //현재 Board 엔티티와 연결, 다음 이미지 세트(imageSet)에 새 이미지를 추가
        ContentImage contentImage = ContentImage.builder ().uuid (uuid).fileName (fileName).content (this).ord (imageSet.size ()).build ();
        imageSet.add (contentImage);

    } // addImage

    public void clearImages () { // Board 엔티티와 연결된 모든 이미지를 제거하는 데 사용
        // imageSet의 각 BoardImage 엔티티에 대해 changeBoard(null)을 호출, 해당 이미지의 board 속성을 제거
        imageSet.forEach (contentImage -> contentImage.changeContent (null));
        this.imageSet.clear ();//imageSet을 비움
    } // clearImages
} // Content
