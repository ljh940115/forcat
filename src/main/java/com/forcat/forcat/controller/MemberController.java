package com.forcat.forcat.controller;

import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    //의존성 주입
    @Autowired
    private final MemberService memberService;

    //로그인 페이지, CustomSecurityConfig로부터 리다이렉트 받음
    @GetMapping("/login")
    public void loginGET(String errorCode, String logout) {
        log.info("loginGET...");
        log.info("logout: " + logout);

        //로그아웃 값이 있을 때 유저 로그아웃
        if (logout != null) {
            log.info("유저 로그아웃");
        }
    }

    //회원가입 페이지
    @GetMapping("/join")
    public void joinGET(){

        log.info("join get...");

    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){

        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {

            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login"; //회원 가입 후 로그인
    }
}



