package com.forcat.forcat.controller;

import com.forcat.forcat.dto.member.MemberJoinDTO;
import com.forcat.forcat.dto.member.MemberUpdateDTO;
import com.forcat.forcat.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller//컨트롤러
@RequestMapping ("/member")
@Log4j2//로그 사용
@RequiredArgsConstructor//final, notnull 필드 생성자 자동 생성
public class MemberController {

    @Autowired    //의존성 주입
    private final MemberService memberService;

    @GetMapping ("/login")//로그인 페이지, CustomSecurityConfig로부터 리다이렉트 받음
    public String loginGET (String errorCode, String logout) {
        log.info ("==========로그인 페이지");
        log.info ("==========로그아웃: " + logout);

        if (logout != null) {        //로그아웃 값이 있을 때 유저 로그아웃
            log.info ("==========유저 로그아웃");
        }
        return "/member/login";
    }

    @GetMapping ("/login/error")//로그인 페이지, CustomSecurityConfig로부터 리다이렉트 받음
    public String loginErrorGET (Model model) {
        model.addAttribute ("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/login";
    }

    @GetMapping ("/join")//회원가입 페이지
    public void joinGET () {
        log.info ("==========회원가입 페이지");
    }

    @PostMapping ("/join")
    public String joinPOST (MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes) {
        log.info ("==========회원가입 실행");
        log.info ("가입 정보 : " + memberJoinDTO);

        try {
            memberService.join (memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            redirectAttributes.addFlashAttribute ("error", "mid");
            return "redirect:/member/join";
        }
        redirectAttributes.addFlashAttribute ("result", "success");
        return "redirect:/member/login"; //회원 가입 후 로그인
    }

    @GetMapping ("/mypage")//마이페이지
    public void myPageGET () {
        log.info ("==========회원수정 페이지");
    }

    @PostMapping ("/mypage")
    public String myPagePOST (MemberUpdateDTO memberUpdateDTO, RedirectAttributes redirectAttributes) {
        log.info ("==========회원수정 실행");
        log.info ("가입 정보 : " + memberUpdateDTO);

        try {
            memberService.update (memberUpdateDTO);
        } catch (MemberService.MidExistException e) {
            redirectAttributes.addFlashAttribute ("error", "mid");
            return "redirect:/member/mypage";
        }
        redirectAttributes.addFlashAttribute ("result", "success");
        return "redirect:/member/mypage"; //회원수정 후 페이지
    }

    @PostMapping ("/delete")
    public String deletePOST (@RequestParam String mid, RedirectAttributes redirectAttributes) {
        log.info ("==========회원삭제 실행");
        log.info ("탈퇴할 ID : " + mid);
        memberService.delete (mid);
        redirectAttributes.addFlashAttribute ("result", "removed");
        return "redirect:/index";
    }
}



