
package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    //소셜 로그인 회원이 아닌 일반 로그인 회원 값만 가져오도록 처리
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.member_id = :member_id and m.social = false")
    Optional<Member> getWithRoles(@Param("member_id") String member_id);

    //소셜 로그인 후처리, 이메일을 이용해 회원 정보 찾기
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(@Param("email") String email);

    /*@EntityGraph(attributePaths = "roleSet")
    Member findByMember_id(@Param("member_id") String member_id);*/

    //소셜 로그인 패스워드 업데이트
    @Modifying
    @Transactional
    @Query("update Member m set m.member_pw =:member_pw where m.member_id = :member_id ")
    void updatePassword(@Param("member_pw") String password, @Param("member_id") String member_id);
}
