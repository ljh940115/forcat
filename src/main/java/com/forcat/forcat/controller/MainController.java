package com.forcat.forcat.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    /*"/"로 요청이 들어오면 index로 이동한다.*/
    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }

    /*메인 페이지*/
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    /*관리자, 파트너사 콘텐츠 게시판*/
    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/content")
    public String content(){
        return "content";
    }

    /*쇼핑몰*/
    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/shop")
    public String shop(){
        return "shop";
    }

    /*커뮤니티, 게시판*/
    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/comm")
    public String comm(){
        return "comm";
    }

    /*기타 기능*/
    @PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/tools")
    public String tools(){
        return "tools";
    }

    /*기타 기능*/
    @GetMapping("/blog")
    public String blog(){
        return "blog";
    }
}
