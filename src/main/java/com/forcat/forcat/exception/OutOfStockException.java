package com.forcat.forcat.exception;

//주문 요청 값보다 상품 재고가 부족할 시 예외처리
public class OutOfStockException extends RuntimeException {

    public OutOfStockException (String message) {
        super (message);
    }
}