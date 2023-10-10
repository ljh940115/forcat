package com.forcat.forcat.controller;

import com.forcat.forcat.dto.shop.OrderDto;
import com.forcat.forcat.dto.shop.OrderHistDto;
import com.forcat.forcat.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @PostMapping (value = "/order")
    public @ResponseBody ResponseEntity order (//ResponseEntity를 반환 메서드
                                               @RequestBody @Valid OrderDto orderDto,//HTTP 본문에 정보를 OrderDto 객체로 전달
                                               BindingResult bindingResult,//유효성 검사 결과 저장
                                               Principal principal) {//현재 세션 사용자 정보를 나타냄
        log.info ("==========주문 메서드 실행");
        log.info ("==========ItemId : " + orderDto.getItemId ());
        log.info ("==========Count : " + orderDto.getCount ());
        if (bindingResult.hasErrors ()) {//유효성 검사 오류 발생 시 실행
            StringBuilder sb = new StringBuilder ();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors ();
            for (FieldError fieldError : fieldErrors) {
                sb.append (fieldError.getDefaultMessage ());
            }
            return new ResponseEntity<String> (sb.toString (), HttpStatus.BAD_REQUEST);//HTTP 응답 반환, 상태코드 400
        }
        log.info ("유효성 검사 성공");

        String mid = principal.getName ();//현재 사용자의 이름을 추출하여 email 변수에 저장
        Long orderId;//주문 ID 저장

        log.info ("현재 사용자 이름 : " + mid);

        try {//주문 서비스를 호출하여 주문을 처리하고, 주문 ID를 반환
            orderId = orderService.order (orderDto, mid);
        } catch (Exception e) {//HTTP 응답 반환, 상태코드 400
            return new ResponseEntity<String> (e.getMessage (), HttpStatus.BAD_REQUEST);
        }
        log.info ("주문 서비스 실행 성공");
        return new ResponseEntity<Long> (orderId, HttpStatus.OK);//주문 성공 시 HTTP 응답 반환, 상태코드 200
    }

    @GetMapping (value = {"/orders", "/orders/{page}"})
    public String orderHist (@PathVariable ("page") Optional<Integer> page, Principal principal, Model model) {
        log.info ("==========주문이력 메서드 실행");
        Pageable pageable = PageRequest.of (page.isPresent () ? page.get () : 0, 4);//한번에 가지고 올 주문 개수 설정
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList (principal.getName (), pageable);//현재 로그인한 회원 이메일, 페이징으로 화면에 전달한 주문 목록 데이터 받음
        log.info ("==========페이징 정보 : " + pageable);
        log.info ("==========ordersHistDtoList : " + ordersHistDtoList);
        log.info ("==========principal : " + principal.getName ());
        model.addAttribute ("orders", ordersHistDtoList);
        model.addAttribute ("page", pageable.getPageNumber ());
        model.addAttribute ("maxPage", 5);
        log.info ("==========model : " + model);
        return "order/orderHist";
    }

    @PostMapping ("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder (@PathVariable ("orderId") Long orderId, Principal principal) {

        if (!orderService.validateOrder (orderId, principal.getName ())) {
            return new ResponseEntity<String> ("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder (orderId);
        return new ResponseEntity<Long> (orderId, HttpStatus.OK);
    }
}