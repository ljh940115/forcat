package com.forcat.forcat.service;

import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.entity.MemberRole;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws member_idExistException{
        //멤버 ID를 추출하여 member_id 변수에 저장
        String member_id = memberJoinDTO.getMember_id();
        //멤버 ID가 이미 데이터베이스에 존재하는지 확인하기 위해 memberRepository를 사용하여 해당 ID의 존재 여부를 확인하고 결과를 exist 변수에 저장
        boolean exist = memberRepository.existsById(member_id);

        //멤버 ID 중복처리
        if(exist){
            throw new member_idExistException();
        }

        //회원 정보를 담는다.
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        //정상인 경우 비밀번호 인코딩
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMember_pw()));
        //유저 권한 추가
        member.addRole(MemberRole.USER);

        //로그 출력
        log.info("=======================");
        log.info(member);
        log.info(member.getRoleSet());

        //회원정보 저장
        memberRepository.save(member);
    }

}
