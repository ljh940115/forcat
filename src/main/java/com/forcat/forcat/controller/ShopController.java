package com.forcat.forcat.controller;

import com.forcat.forcat.dto.ItemSearchDto;
import com.forcat.forcat.dto.MainItemDto;
import com.forcat.forcat.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller//컨트롤러
@RequestMapping("/shop")
@Log4j2//로그 사용
@RequiredArgsConstructor//final, notnull 필드 생성자 자동 생성
public class ShopController {

    private final ItemService itemService;

    @GetMapping("/")//메인 페이지
    public String shop(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "shop";
    }
}
