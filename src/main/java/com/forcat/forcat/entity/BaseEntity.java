package com.forcat.forcat.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//모든 테이블에 사용할 데이터 설정, 상속하여 사용한다.
@MappedSuperclass//공통 상속 클래스 명시
@EntityListeners (value = {AuditingEntityListener.class})//해당 클래스에 Auditing 기능 포함, 엔티티가 DB에 추가될 때 자동으로 시간 값 저장
@Getter
abstract class BaseEntity {

    @CreatedDate//등록일
    @Column (name = "regdate", updatable = false)//수정 불가
    private LocalDateTime regDate;

    @LastModifiedDate//수정일
    @Column (name = "moddate")
    private LocalDateTime modDate;

}
