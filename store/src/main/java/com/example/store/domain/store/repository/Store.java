package com.example.store.domain.store.repository;

import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreCategory;
import com.example.store.domain.store.repository.enums.StoreStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store{

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_id", nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Embedded
  @Column(nullable = false)
  private Address address;

  @Column(nullable = false)
  private String phone;

  @Column(name = "business_number", nullable = false)
  private String businessNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StoreCategory category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OperatingStatus operatingStatus;

  @Embedded
  @Column(nullable = false)
  private OperatingTime operatingTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StoreStatus storeStatus;

  @Column(nullable = false)
  private LocalDateTime registeredAt;

  @Column(nullable = true)
  private LocalDateTime unregisteredAt;

  @Column(name = "user_id")
  private Long userId;

  public void register() {
    this.registeredAt = LocalDateTime.now();
    this.storeStatus = StoreStatus.REGISTERED;
  }

  public void unregister() {
    this.unregisteredAt = LocalDateTime.now();
    this.storeStatus = StoreStatus.UNREGISTERED;
  }

  public boolean isOperating() {
    if (operatingStatus == OperatingStatus.DAY_OFF) {
      return false;
    }

    if (operatingTime.isOpenAllDay()) {
      return true;
    }

    LocalTime now = LocalTime.now();
    LocalTime open = this.operatingTime.getOpeningTime();
    LocalTime close = this.operatingTime.getClosingTime();

    if(!now.isAfter(close)) {
      return !now.isBefore(open) && !now.isAfter(close);
    }

    return !now.isBefore(open) || !now.isAfter(close);
  }

  public void update(
      String name,
      Address address,
      String phone,
      StoreCategory category,
      LocalTime openingTime,
      LocalTime closingTime
  ) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    this.category = category;
    this.operatingTime.setOpeningTime(openingTime);
    this.operatingTime.setClosingTime(closingTime);

  }
}

