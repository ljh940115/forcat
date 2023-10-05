package com.forcat.forcat.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//엔티티 클래스 필수 사항 @Entity, @Id
//실제 DB와 매칭할 클래스
@Entity//엔티티 클래스
@Getter
@Builder//빌더 클래스
@AllArgsConstructor//모든 필드값을 파라미터로 받는 생성자로 만듦
@NoArgsConstructor//값 없는 기본 생성자 생성
@ToString(exclude = "roleSet")//객체 값을 문자열로 리턴
public class Member extends BaseEntity{

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="member_id")
    private String member_id;//회원 아이디
    private String member_pw;//회원 비밀번호
    private String name;
    private String password;
    private String address;
    //@Column(unique = true)
    private String email;//회원 이메일
    private boolean del;//회원 탈퇴 여부
    private boolean social;//소셜 로그인 회원가입 여부

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String member_pw ){
        this.member_pw = member_pw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){this.social = social;}
}
