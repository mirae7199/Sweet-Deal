package com.mirae.item.domain.item.entity;

import com.mirae.global.errorcode.ItemErrorCode;
import com.mirae.global.exception.BusinessException;
import com.mirae.item.domain.item.entity.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "item_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name="expired_at")
    private LocalDateTime expiredAt;

    @Column(name="registered_at")
    private LocalDateTime registeredAt;

    @Column(name="unregistered_at")
    private LocalDateTime unregisteredAt;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Column(name = "original_price", nullable = false)
    private Integer originalPrice;

    @Column(nullable = false)
    private Integer discountPrice;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="order_id")
    private Long orderId;

    public void register() {
        this.status = ItemStatus.SALE;
        this.registeredAt = LocalDateTime.now();
    }

    public void unregister() {
        this.status = ItemStatus.DELETED;
        this.quantity = 0;
        this.unregisteredAt = LocalDateTime.now();

    }

    public void changeStatus(ItemStatus status) {
        this.status = status;
    }
    public void updateQuantity(Integer quantity) {
        if(quantity <= 0) {
            throw new BusinessException(ItemErrorCode.INVALID_ITEM_QUANTITY);
        }
        this.quantity = quantity;
    }

    public void rename(String name) {
        if(name == null || name.isEmpty()) {
            throw new BusinessException(ItemErrorCode.INVALID_ITEM_NAME);
        }

        this.name = name;
    }

    public void updateExpiredAt(LocalDateTime expiredAt) {
        LocalDateTime present = LocalDateTime.now();

        if(expiredAt.isBefore(present)) {
            throw new BusinessException(ItemErrorCode.INVALID_ITEM_EXPIRED_DATE);
        }

        this.expiredAt = expiredAt;
    }

    public void updatePrice(Integer price) {
        if(price < 0) {
            throw new BusinessException(ItemErrorCode.INVALID_ITEM_PRICE);
        }

        this.discountPrice = price;
    }

    public Integer remainingQuantity(Integer quantity) {
        if(this.quantity < quantity) {
            throw new BusinessException(ItemErrorCode.INSUFFICIENT_ITEM_QUANTITY);
        }
        return this.quantity - quantity;
    }

    public void decreaseStock(Integer quantity) {
       this.quantity -= quantity;
    }

    public void cancelStock(Integer quantity) {
        this.quantity += quantity;
    }

    public void setOrder(Long orderId) {
        this.orderId = orderId;
    }

    public void insertOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
