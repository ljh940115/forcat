package com.forcat.forcat.service;

import com.forcat.forcat.dto.shop.OrderDto;
import com.forcat.forcat.dto.shop.OrderHistDto;
import com.forcat.forcat.dto.shop.OrderItemDto;
import com.forcat.forcat.entity.*;
import com.forcat.forcat.repository.ItemRepository;
import com.forcat.forcat.repository.MemberRepository;
import com.forcat.forcat.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.forcat.forcat.repository.ItemImgRepository;
import org.springframework.data.domain.Pageable;

import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order (OrderDto orderDto, String email) {
        Item item = itemRepository.findById (orderDto.getItemId ()).orElseThrow (EntityNotFoundException::new);
        Optional<Member> memberOptional = memberRepository.findByMid (email);
        Member member = memberOptional.orElseThrow (EntityNotFoundException::new);
        List<OrderItem> orderItemList = new ArrayList<> ();
        OrderItem orderItem = OrderItem.createOrderItem (item, orderDto.getCount ());
        orderItemList.add (orderItem);
        Order order = Order.createOrder (member, orderItemList);
        orderRepository.save (order);
        return order.getId ();
    }

    @Transactional (readOnly = true)
    public Page<OrderHistDto> getOrderList (String mid, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders (mid, pageable);//유저 ID와 페이징 조건을 이용하여 주문 목록 조회
        Long totalCount = orderRepository.countOrder (mid);//유저의 주문 총 개수를 조회
        List<OrderHistDto> orderHistDtos = new ArrayList<> ();
        for (Order order : orders) {//주문 리스트를 반복해 구매 이력 페이지에 전달할 DTO 생성
            OrderHistDto orderHistDto = new OrderHistDto (order);
            List<OrderItem> orderItems = order.getOrderItems ();
            for (OrderItem orderItem : orderItems) {//주문 상품의 대표 이미지 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn (orderItem.getItem ().getId (), "Y");
                OrderItemDto orderItemDto = new OrderItemDto (orderItem, itemImg.getImgUrl ());
                orderHistDto.addOrderItemDto (orderItemDto);
            }
            orderHistDtos.add (orderHistDto);
        }//페이지 구현 객체 생성하여 반환
        return new PageImpl<OrderHistDto> (orderHistDtos, pageable, totalCount);
    }

    @Transactional (readOnly = true)
    public boolean validateOrder (Long orderId, String mid) {
        Optional<Member> memberOptional = memberRepository.findByMid (mid);
        Member curMember = memberOptional.orElseThrow (EntityNotFoundException::new);
        Order order = orderRepository.findById (orderId).orElseThrow (EntityNotFoundException::new);
        Member savedMember = order.getMember ();
        return StringUtils.equals (curMember.getMid (), savedMember.getMid ());
    }

    public void cancelOrder (Long orderId) {
        Order order = orderRepository.findById (orderId).orElseThrow (EntityNotFoundException::new);
        order.cancelOrder ();
    }

    public Long orders (List<OrderDto> orderDtoList, String mid) {
        Optional<Member> memberOptional = memberRepository.findByMid (mid);
        Member member = memberOptional.orElseThrow (EntityNotFoundException::new);
        List<OrderItem> orderItemList = new ArrayList<> ();
        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById (orderDto.getItemId ()).orElseThrow (EntityNotFoundException::new);
            OrderItem orderItem = OrderItem.createOrderItem (item, orderDto.getCount ());
            orderItemList.add (orderItem);
        }
        Order order = Order.createOrder (member, orderItemList);
        orderRepository.save (order);
        return order.getId ();
    }
}