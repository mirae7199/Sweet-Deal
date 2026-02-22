package com.mirae.order.domain.order.controller.model.response;

import com.mirae.order.domain.order.repository.OrderItem;
import com.mirae.order.domain.order.repository.enums.OrderStatus;
import com.mirae.order.domain.order.repository.enums.PaymentMethod;
import com.mirae.order.domain.order.repository.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderRegisterResponse {

  private Long id;
  private LocalDateTime orderAt;
  private LocalDateTime cancelledAt;
  private OrderStatus orderStatus;
  private Long totalPrice;

  private List<OrderItem> orderItems;
  private Long itemCount; // 상품의 종류 개수
  private Long totalItemQuantity; // 각 상품의 총 개수

  private Long paymentId;
  private PaymentStatus paymentStatus;
  private PaymentMethod paymentMethod;
  private LocalDateTime paymentAt;
  private Long totalPayPrice;
  private boolean paymentSuccess;

}
