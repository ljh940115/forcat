package com.forcat.forcat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table (name = "persistent_logins")//해당 테이블과 매핑
public class PersistentLogin {

    @Id//PK 지정
    @Column (name = "series", length = 64)//테이블 매핑, 문자열 길이 64자
    private String series;
    @Column (name = "username", length = 64, nullable = false)//테이블 매핑, 문자열 길이 64자, not null
    private String username;
    @Column (name = "token", length = 64, nullable = false)//테이블 매핑, 문자열 길이 64자, not null
    private String token;
    @Column (name = "last_used", nullable = false)//테이블 매핑, 문자열 길이 64자, not null
    private Timestamp lastUsed;//생성 시간 자동 기록
}