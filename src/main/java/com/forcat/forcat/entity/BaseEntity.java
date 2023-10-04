package com.forcat.forcat.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})
// 엔티티의 이벤트를 감지하고 처리할 리스너 클래스 지정
// 엔티티에 변화가 생기면 특정 동작이 실행되도록 설정 가능
@MappedSuperclass
// 테이블과 직접 매핑되지 않고 하위 엔티티 클래스에 상속되어 사용 됨
// 공통 매핑 정보가 필요할 때 사용
@Getter
public abstract class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false) // 필드의 값 변경되어도 update 되지 않음
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
