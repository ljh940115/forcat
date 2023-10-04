package com.forcat.forcat.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
// 엔티티의 이벤트를 감지하고 처리할 리스너 클래스 지정
// 엔티티에 변화가 생기면 특정 동작이 실행되도록 설정 가능
@MappedSuperclass
// 테이블과 직접 매핑되지 않고 하위 엔티티 클래스에 상속되어 사용 됨
// 공통 매핑 정보가 필요할 때 사용
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성될 때 사용
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티가 수정될 때 사용
    private LocalDateTime updateTime;

}
