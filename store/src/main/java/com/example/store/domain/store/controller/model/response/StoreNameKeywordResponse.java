package com.example.store.domain.store.controller.model.response;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.OperatingTime;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import lombok.Builder;

@Builder
public record StoreNameKeywordResponse(
    Long storeId,
    String name,
    Address address,
    String phone,
    OperatingStatus operatingStatus,
    OperatingTime operatingTime
) {

}
