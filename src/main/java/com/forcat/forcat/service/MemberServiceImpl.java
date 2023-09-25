package org.zerock.b01.service;

import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.repository.MemberRepository;
import com.forcat.forcat.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.entity.MemberRole;
import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.repository.MemberRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException{
        //mid가 존재하는 경우에 예외 발생
        String mid = memberJoinDTO.getMid();

        boolean exist = memberRepository.existsById(mid);

        if(exist){
            throw new MidExistException();
        }

        //회원 정보를 담는다.
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        //정상인 경우 비밀번호 인코딩
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        //유저 권한 추가
        member.addRole(MemberRole.USER);

        //로그 출력
        log.info("=======================");
        log.info(member);
        log.info(member.getRoleSet());

        //
        memberRepository.save(member);

    }
}
