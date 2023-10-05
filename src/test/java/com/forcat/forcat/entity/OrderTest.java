package com.forcat.forcat.entity;

import com.forcat.forcat.constant.ItemSellStatus;
import com.forcat.forcat.repository.ItemRepository;
import com.forcat.forcat.repository.MemberRepository;
import com.forcat.forcat.repository.OrderItemRepository;
import com.forcat.forcat.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    public Order createOrder() {
        Order order = new Order();

        for(int i = 0; i < 3; i++) {
            Item item = createItem(); // Item 엔티티 생성하여 item으로 저장
            itemRepository.save(item); // itemRepository를 통해 item 저장
            OrderItem orderItem = new OrderItem(); // OrderItem 엔티티 생성하여 orderItem에 저장
            orderItem.setItem(item); // orderItem에 item 연관 짓기
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order); // orderItem에 order 연관 짓기
            order.getOrderItems().add(orderItem); // order 엔티티의 컬렉션에 orderItem 추가
        }

        Member member = new Member(); // Member 엔티티 생성하여 member로 저장
        memberRepository.save(member); // MemberRepository를 통해 member 저장

        order.setMember(member); // order에 member 연관 짓기
        orderRepository.save(order); // orderRepository를 통해 order 저장
        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order(); // Order 엔티티 생성하여 order로 저장

        for(int i = 0; i < 3; i++) {
            Item item = this.createItem(); // Item 엔티티를 생성하여 item으로 저장
            itemRepository.save(item); // itemRepository로 저장 작업
            OrderItem orderItem = new OrderItem(); // OrderItem 엔티티 생성하여 orderItem으로 저장
            orderItem.setItem(item); // orderItem에 item 연관 짓기
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order); // orderItem에 order 연관 짓기
            order.getOrderItems().add(orderItem); // order 엔티티의 컬렉션에 orderItem 추가
        }

        orderRepository.saveAndFlush(order);
        // 저장 후 flush하여 DB에 반영
        em.clear();
        // 영속성컨텍스트 초기화 -> 관리 중인 엔티티 지우기

        Order savedOrder = orderRepository.findById(order.getId())
                                            .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
        // assertEquals : 두 객체의 값이 같은지?
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder(); // Order 엔티티 생성하여 order로 저장
        order.getOrderItems().remove(1);
        // order의 OrderItems 리스트 중 0번째 요소를 삭제
        em.flush(); // 영속성 컨텍스트 내용 반영
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder(); // Order 엔티티 생성하여 order에 저장
        Long orderItemId = order.getOrderItems().get(1).getId();
        // order의 OrderItems 리스트 중 0번째 요소의 id 가져와서 orderItemId에 저장
        em.flush(); // 영속성 컨텍스트 내용 반영
        em.clear(); // 영속성 컨텍스트 초기화

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                                                   .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        // orderItem의 Order 객체 클래스 출력
        System.out.println("===========================");
        orderItem.getOrder().getOrderDate(); // orderItem의 Order 객체 클래스의 주문일 조회
        System.out.println("===========================");
    }

}
