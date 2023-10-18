package com.forcat.forcat.dto.shop;

import com.forcat.forcat.entity.OrderStatus;
import com.forcat.forcat.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {//주문 정보 : 주문 값과 주문 상품 정보를 가져온다.

    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문 상태
    private List<OrderItemDto> orderItemDtoList = new ArrayList<> ();//주문 상품 리스트
    public OrderHistDto (Order order) {
        this.orderId = order.getId ();
        this.orderDate = order.getOrderDate ().format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus ();
    }

    public void addOrderItemDto (OrderItemDto orderItemDto) {
        orderItemDtoList.add (orderItemDto);
    }//OrderItemDto을 주문 상품 리스트에 추가
}