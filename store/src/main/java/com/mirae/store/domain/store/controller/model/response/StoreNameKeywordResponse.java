package com.mirae.store.domain.store.controller.model.response;

import com.mirae.store.domain.store.repository.Address;
import com.mirae.store.domain.store.repository.OperatingTime;
import com.mirae.store.domain.store.repository.enums.OperatingStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreNameKeywordResponse {
  private Long storeId;
  private String name;
  private Address address;
  private String phone;
  private OperatingStatus operatingStatus;
  private OperatingTime operatingTime;

}
