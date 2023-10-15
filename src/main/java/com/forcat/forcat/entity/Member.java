package com.forcat.forcat.entity;

import com.forcat.forcat.dto.member.MemberJoinDTO;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString (exclude = "roleSet")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    private String mid;//회원 아이디
    private String mpw;//회원 비밀번호
    private String name;//회원이름
    private String email;//회원 이메일
    private String address;//회원 주소
    private boolean del;//회원 탈퇴 여부
    private boolean social;//소셜 로그인 회원가입 여부

    @OneToMany (mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Board> boards = new HashSet<> ();

    @ElementCollection (fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<> ();

    public static Member createMember (MemberJoinDTO memberJoinDTO, PasswordEncoder passwordEncoder) {
        return Member.builder ().mid (memberJoinDTO.getMid ()).name (memberJoinDTO.getName ()).email (memberJoinDTO.getEmail ()).address (memberJoinDTO.getAddress ()).mpw (passwordEncoder.encode (memberJoinDTO.getMpw ())).build ();
    }

    public void changePassword (String mpw) { this.mpw = mpw; }//비밀번호 변경하는 메서드

    public void changeEmail (String email) { this.email = email; }//이메일 주소 변경하는 메서드

    public void changeDel (boolean del) { this.del = del; }//회원 탈퇴 여부를 변경하는 메서드

    public void addRole (MemberRole memberRole) { this.roleSet.add (memberRole); }//회원 역할 추가하는 메서드

    public void clearRoles () {
        this.roleSet.clear ();
    }//회원 역할 초기화 메서드

    public void changeSocial (boolean social) { this.social = social; }//소셜 로그인 가입 여부 변경 메서드
}
