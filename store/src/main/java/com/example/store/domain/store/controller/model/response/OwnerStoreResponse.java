package com.example.store.domain.store.controller.model.response;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.enums.StoreCategory;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record OwnerStoreResponse(
    Long storeId,
    String name,
    Address address,
    String phone,
    String businessNumber,
    StoreCategory category,
    StoreStatus status,
    LocalDateTime registeredAt

) {

}
