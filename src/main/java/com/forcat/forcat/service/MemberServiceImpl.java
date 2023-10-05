package com.forcat.forcat.service;

import com.forcat.forcat.dto.member.MemberJoinDTO;
import com.forcat.forcat.dto.member.MemberUpdateDTO;
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
public
class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException{
        String mid = memberJoinDTO.getMid();//memberJoinDTO 객체에서 회원 아이디(mid)를 가져옴
        boolean exist = memberRepository.existsById(mid);//회원 아이디(mid)가 이미 존재하는지 확인
        if(exist){throw new MidExistException();}//이미 회원 아이디(mid)가 존재하는 경우 MidExistException 예외 처리

        Member member = modelMapper.map(memberJoinDTO, Member.class);//회원 정보를 담는다.
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));//정상인 경우 비밀번호 인코딩
        member.addRole(MemberRole.USER);  //유저 권한 추가

        //로그 출력
        log.info("=======================");
        log.info(member);
        log.info(member.getRoleSet());
        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDTO memberUpdateDTO) throws MidExistException {
        String mid = memberUpdateDTO.getMid();
        String mpw = memberUpdateDTO.getMpw();
        String email = memberUpdateDTO.getEmail();
        Member member = modelMapper.map(memberUpdateDTO, Member.class);//회원 정보를 담는다
        member.changePassword(passwordEncoder.encode(memberUpdateDTO.getMpw()));//정상인 경우 비밀번호 인코딩
        memberRepository.updateMemberData(mid, mpw, email);
        //로그 출력
        log.info("=======================");
        log.info(member);
        log.info(member.getRoleSet());
        memberRepository.save(member);
    }

    @Override
    public void delete(String mid) {
        // 이메일을 사용하여 회원 정보 조회
        // 회원 정보가 존재하는 경우 삭제
        memberRepository.deleteByMid(mid);
    }
}
