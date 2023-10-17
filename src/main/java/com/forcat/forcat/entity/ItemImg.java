package com.forcat.forcat.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table (name = "item_img")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemImg extends BaseEntity {

    @Id
    @Column (name = "item_img_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로
    private String repImgYn; // 대표 이미지 여부

    @ManyToOne (cascade = {CascadeType.ALL}, fetch = FetchType.LAZY) // Item 엔티티와 다대일 단방향 매핑
    @JoinColumn (name = "item_id")
    private Item item;

    public void updateItemImg (String oriImgName, String imgName, String imgUrl) {//이미지 정보 수정 메서드
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
