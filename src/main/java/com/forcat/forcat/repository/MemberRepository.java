package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Board;
import com.forcat.forcat.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph (attributePaths = "roleSet")
    @Query ("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles (@Param ("mid") String mid);//소셜 로그인 회원이 아닌 일반 로그인 회원 값만 가져오도록 처리

    @EntityGraph (attributePaths = "roleSet")
    Optional<Member> findByEmail (@Param ("email") String email);//소셜 로그인 후처리, 이메일을 이용해 회원 정보 찾기

    @Query ("select m from Member m where m.mid = :mid ")
    Optional<Member> findByMid (@Param ("mid") String mid);

    @Modifying
    @Transactional
    @Query ("update Member m set m.mpw =:mpw where m.mid = :mid ")
    void updatePassword (@Param ("mpw") String password, @Param ("mid") String mid);//소셜 로그인 패스워드 업데이트

    @Modifying
    @Transactional
    @Query ("update Member m set m.mpw =:mpw, m.email =:email where m.mid = :mid ")
    void updateMemberData (@Param ("mid") String mid, @Param ("mpw") String mpw, @Param ("email") String email);//회원 정보 수정(비밀번호, 이메일)

    @Transactional
    void deleteByMid (String mid);// 멤버 1명을 삭제

    @EntityGraph (attributePaths = {"imageSet"})
    @Query ("select b from Board b where b.bno =:bno")
    Optional<Board> findByIdWithImages (@Param ("bno") Long bno);

/*    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid")
    Optional<Member> findByIdWithBoard(@Param("mid") String mid);*/
}
