package com.forcat.forcat.controller;

import com.forcat.forcat.dto.shop.CartDetailDto;
import com.forcat.forcat.dto.shop.CartItemDto;
import com.forcat.forcat.dto.shop.CartOrderDto;
import com.forcat.forcat.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import org.springframework.ui.Model;

@Controller
@Log4j2//로그 사용

@RequiredArgsConstructor//final, notnull 필드 생성자 자동 생성
public class CartController {

    private final CartService cartService;

    @PostMapping (value = "/cart")
    public @ResponseBody ResponseEntity order (@RequestBody @Valid CartItemDto cartItemDto, //HTTP 요청 본문에서 JSON 데이터를 추출하고 유효성 검사를 수행
                                               BindingResult bindingResult, Principal principal) {//유효성 검사 결과 저장, 현재 인증된 사용자의 정보 제공
        log.info ("==========장바구니 상품 추가 실행");
        if (bindingResult.hasErrors ()) {
            StringBuilder sb = new StringBuilder ();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors ();
            for (FieldError fieldError : fieldErrors) {
                sb.append (fieldError.getDefaultMessage ());
            }
            return new ResponseEntity<String> (sb.toString (), HttpStatus.BAD_REQUEST);
        }
        String mid = principal.getName ();
        Long cartItemId;
        try {
            cartItemId = cartService.addCart (cartItemDto, mid);
        } catch (Exception e) {
            return new ResponseEntity<String> (e.getMessage (), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long> (cartItemId, HttpStatus.OK);
    }

    @GetMapping (value = "/cart")//장바구니 목록 표시
    public String orderHist (Principal principal, Model model) {//현재 인증된 사용자의 정보를 제공, 뷰에 전달하기 위한 모델 객체를 받음
        log.info ("==========장바구니 상품 목록 출력");
        List<CartDetailDto> cartDetailList = cartService.getCartList (principal.getName ());//사용자의 장바구니 목록을 가져온 후, 모델에 추가하여 화면에 표시
        model.addAttribute ("cartItems", cartDetailList);//"cartItems"라는 이름으로 cartDetailList를 추가
        return "cart/cartList";
    }

    @PatchMapping (value = "/cartItem/{cartItemId}")//HTTP PATCH 요청이 엔드포인트로 이 메서드를 호출할 수 있도록 설정하여 장바구니 항목 수정
    public @ResponseBody ResponseEntity updateCartItem (@PathVariable ("cartItemId") Long cartItemId,//URL 경로에서 cartItemId를 추출
                                                        int count, Principal principal) {//요청 본문에서 상품 수량을 추출, 현재 인증된 사용자의 정보를 제공
        log.info ("==========장바구니 상품 수량 수정 실행");
        if (count <= 0) {//수량 확인
            return new ResponseEntity<String> ("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem (cartItemId, principal.getName ())) {
            return new ResponseEntity<String> ("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.updateCartItemCount (cartItemId, count);//장바구니 수량 수정
        return new ResponseEntity<Long> (cartItemId, HttpStatus.OK);//장바구니 수량 반환
    }

    @DeleteMapping (value = "/cartItem/{cartItemId}")//HTTP DELETE 요청이 엔드포인트로 이 메서드를 호출할 수 있도록 설정하여 장바구니 특정 항목 삭제
    public @ResponseBody ResponseEntity deleteCartItem (@PathVariable ("cartItemId") Long cartItemId, Principal principal) {////URL 경로에서 cartItemId를 추출, 현재 인증된 사용자의 정보를 제공
        log.info ("==========장바구니 특정 상품 삭제");
        if (!cartService.validateCartItem (cartItemId, principal.getName ())) {//삭제하려는 장바구니 항목에 대한 권한을 검사
            return new ResponseEntity<String> ("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem (cartItemId);//장바구니에서 해당 항목을 삭제
        return new ResponseEntity<Long> (cartItemId, HttpStatus.OK);
    }

    @PostMapping (value = "/cart/orders")//HTTP POST 요청이 엔드포인트로 이 메서드를 호출할 수 있도록 설정하여 장바구니 특정 항목 주문
    public @ResponseBody ResponseEntity orderCartItem (@RequestBody CartOrderDto cartOrderDto, Principal principal) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList ();//주문할 장바구니 항목들의 목록을 추출
        if (cartOrderDtoList == null || cartOrderDtoList.size () == 0) {//주문할 항목이 없거나 목록이 비어 있는 경우
            return new ResponseEntity<String> ("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }
        for (CartOrderDto cartOrder : cartOrderDtoList) {//주문할 항목을 반복하여 찾음
            if (!cartService.validateCartItem (cartOrder.getCartItemId (), principal.getName ())) {//주문 권한을 확인
                return new ResponseEntity<String> ("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = cartService.orderCartItem (cartOrderDtoList, principal.getName ());//주문이 가능한 모든 항목에 대해 주문을 생성
        return new ResponseEntity<Long> (orderId, HttpStatus.OK);
    }
}