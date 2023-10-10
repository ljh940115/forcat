package com.forcat.forcat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Log4j2//로그 출력
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    //@PreAuthorize("hasRole('USER')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/")//"/"로 요청이 들어오면 메인 페이지로 이동
    public String redirectToIndex() { log.info("==========MainCon.../"); return "redirect:/index";}

    @GetMapping("/index")//메인 페이지
    public String index(){ log.info("==========MainCon...idnex"); return "index";}

    @GetMapping("/content")//관리자, 파트너사 콘텐츠 게시판
    public String content(){ log.info("==========MainCon...content");return "content";}

    @GetMapping("/shop")//쇼핑몰
    public String shop(){log.info("==========MainCon...shop");return "shop";}

    @GetMapping("/comm")//커뮤니티, 게시판
    public String comm(){log.info("==========MainCon...comm");return "comm";}

    @GetMapping("/tools")//기타 기능
    public String tools(){log.info("==========MainCon...tools");return "tools";}

    @GetMapping("/blog")//기타 기능
    public String blog(){log.info("==========MainCon...blog");return "blog";}

    @GetMapping("/map")//기타 기능
    public String map(){log.info("==========MainCon...blog");return "map";}
}
