package com.forcat.forcat.service;

import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.dto.ReplyDTO;
import com.forcat.forcat.entity.Reply;
import com.forcat.forcat.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service//서비스
@RequiredArgsConstructor//Final 선언 필드 생성자 자동 생성
@Log4j2//로그 사용
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;//댓글 db 저장 검색
    private final ModelMapper modelMapper;//객체 간 매핑 작업, DTO와 ENTITY 사이 반환

    @Override
    public Long register(ReplyDTO replyDTO) {//ReplyDTO 객체를 받아 댓글을 등록하고 RNO 반환
        Reply reply = modelMapper.map(replyDTO, Reply.class);//DTO를 ENTITY로 매핑, DB 복사
        Long rno = replyRepository.save(reply).getRno();//댓글 저장 및 RNO 반환
        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {//rno를 받아 조회
        Optional<Reply> replyOptional = replyRepository.findById(rno);//rno와 같은 댓글 DB에서 찾는다.
        Reply reply = replyOptional.orElseThrow();//댓글이 존재하지 않는 경우 예외처리 전달
        return modelMapper.map(reply, ReplyDTO.class);//조회한 댓글을 modelMapper.map을 사용하여 ReplyDTO로 변환한 후 반환
    }

    @Override
    public void modify(ReplyDTO replyDTO) {//ReplyDTO를 받아 조회
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());//rno와 같은 댓글을 DB에서 찾는다.
        Reply reply = replyOptional.orElseThrow();//댓글이 존재하지 않는 경우 예외처리 전달
        reply.changeText(replyDTO.getReplyText());//댓글 내용을 주어진 replyDTO 내용으로 변경
        replyRepository.save(reply);//변경한 댓글을 DB에 저장
    }

    @Override
    public void remove(Long rno) {//rno를 받아 조회
        replyRepository.deleteById(rno);//rno에 해당하는 댓글 삭제
    }

    @Override//특정 게시물 댓글 목록을 페이징 후 조회, ReplyDTO 형태로 변환하여 반환
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        //페이지 설정, 페이지 번호, 페이지 크기를 가져옴
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());
        //주어진 bno에 해당하는 댓글 목록을 페이징하여 조회, result에 저장
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        List<ReplyDTO> dtoList =//댓글 ENTITY를 DTO로 변환
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()//PageResponseDTO 객체 반환
                .pageRequestDTO(pageRequestDTO)//페이지 요청 정보 설정
                .dtoList(dtoList)//댓글 목록 설정
                .total((int)result.getTotalElements())//전체 결과 수를 설정
                .build();
    }
}
