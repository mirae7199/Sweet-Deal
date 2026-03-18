package com.example.store.domain.store.converter;

import com.example.global.annotation.Converter;
import com.example.store.domain.store.controller.model.response.NearbyStoreResponse;
import com.example.store.domain.store.controller.model.response.NearbyStoresResponse;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.response.OwnerStoreResponse;
import com.example.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.example.store.domain.store.controller.model.response.StoreNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.StoreSimpleResponse;
import com.example.store.domain.store.controller.model.response.StoresNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.UserStoreResponse;
import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import java.math.BigDecimal;
import java.util.List;

@Converter
public class StoreConverter {

  public Store toEntity(StoreRegisterRequest request, Long userId) {
    Store store = Store.builder()
        .userId(userId)
        .name(request.getName())
        .address(request.getAddress())
        .phone(request.getPhone())
        .businessNumber(request.getBusinessNumber())
        .category(request.getCategory())
        .operatingTime(request.getOperatingTime())
        .build();

    if (store.isOperating()) {
      store.setOperatingStatus(OperatingStatus.OPEN);
    }
    return store;
  }

  public NearbyStoresResponse toNearbyStoreResponse(List<Store> stores, BigDecimal radiusInMeters) {
    List<NearbyStoreResponse> nearbyStoreResponses = stores.stream().map(
        store -> NearbyStoreResponse.builder()
            .id(store.getId())
            .sido(store.getAddress().getSido())
            .sigungu(store.getAddress().getSigungu())
            .eupMyeonDong(store.getAddress().getSigungu())
            .latitude((store.getAddress().getLatitude()))
            .longitude(store.getAddress().getLongitude())
            .radiusInMeters(radiusInMeters)
            .build()
    ).toList();

    return NearbyStoresResponse.builder()
        .nearbyStoreResponses(nearbyStoreResponses)
        .build();
  }

  public StoreSimpleResponse toStoreSimpleResponse(List<Store> stores) {
    List<Long> storesId = stores.stream().map(store -> store.getId()).toList();

    StoreSimpleResponse storeSimpleResponse = new StoreSimpleResponse(storesId);

    return storeSimpleResponse;
  }

  public StoresNameKeywordResponse toStoreNameKeywordResponse(List<Store> stores) {
    List<StoreNameKeywordResponse> storeNameKeywordResponses = stores.stream().map(
        store -> StoreNameKeywordResponse.builder()
            .storeId(store.getId())
            .name(store.getName())
            .address(store.getAddress())
            .phone(store.getPhone())
            .operatingStatus(store.getOperatingStatus())
            .operatingTime(store.getOperatingTime())
            .build()
    ).toList();

    return StoresNameKeywordResponse.builder()
        .storeNameKeywordResponses(storeNameKeywordResponses)
        .build();
  }

  public OwnerStoresResponse toOwnerStoresResponse(List<Store> stores) {
    List<OwnerStoreResponse> ownerStoreResponses = stores.stream().map(
        store -> OwnerStoreResponse.builder()
            .storeId(store.getId())
            .name(store.getName())
            .address(store.getAddress())
            .phone(store.getPhone())
            .businessNumber(store.getBusinessNumber())
            .category(store.getCategory())
            .status(store.getStoreStatus())
            .registeredAt(store.getRegisteredAt())
            .build()
    ).toList();

    return OwnerStoresResponse.builder()
        .ownerStoreResponses(ownerStoreResponses)
        .build();

  }

  public UserStoreResponse toUserStoreResponse(Store store) {
    return UserStoreResponse.builder()
        .storeId(store.getId())
        .name(store.getName())
        .address(store.getAddress())
        .phone(store.getPhone())
        .businessNumber(store.getBusinessNumber())
        .category(store.getCategory())
        .operatingStatus(store.getOperatingStatus())
        .operatingTime(store.getOperatingTime())
        .build();
  }
}
