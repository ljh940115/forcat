package com.forcat.forcat.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @PreAuthorize("hasRole('USER')")//메인 페이지는 USER 권한 접속 가능
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/content")
    public String content(){
        return "content";
    }

    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/shop")
    public String shop(){
        return "shop";
    }

    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/comm")
    public String comm(){
        return "comm";
    }

    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/tools")
    public String tools(){
        return "tools";
    }

    @PreAuthorize("hasRole('USER')")//test 페이지는 MEMBER 권한 접속 가능
    @GetMapping("/test")
    public String test(Model model){
        model.addAttribute("data", "타임리프 테스트");
        return "thymeleafEx";
    }

}
