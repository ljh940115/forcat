package com.forcat.forcat.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // Member 엔티티와 일대일 매핑
    // 일대일 매핑은 옵션 지정하지 않으면 (fetch = FetchType.EAGER) 자동 지정
    @JoinColumn(name="mid") // 매핑할 외래키 지정
    private Member member;
}
