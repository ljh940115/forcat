package com.forcat.forcat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    //로그인 페이지, CustomSecurityConfig로부터 리다이렉트 받음
    @GetMapping("/login")
    public void loginGET(String error, String logout) {
    log.info("loginGET...");
    log.info("logout: " + logout);
    }
}



