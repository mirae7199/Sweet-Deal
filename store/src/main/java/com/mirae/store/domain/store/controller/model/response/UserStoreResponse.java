package com.mirae.store.domain.store.controller.model.response;

import com.mirae.store.domain.store.repository.Address;
import com.mirae.store.domain.store.repository.OperatingTime;
import com.mirae.store.domain.store.repository.enums.OperatingStatus;
import com.mirae.store.domain.store.repository.enums.StoreCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserStoreResponse {
  private Long storeId;
  private String name;
  private Address address;
  private String phone;
  private String businessNumber;
  private StoreCategory category;
  private OperatingStatus operatingStatus;
  private OperatingTime operatingTime;

}
