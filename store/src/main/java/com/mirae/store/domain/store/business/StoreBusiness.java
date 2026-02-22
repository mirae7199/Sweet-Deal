package com.mirae.store.domain.store.business;

import com.mirae.global.anntation.Business;
import com.mirae.global.errorcode.StoreErrorCode;
import com.mirae.store.domain.common.exception.IsBlankException;
import com.mirae.store.domain.store.controller.model.request.RegisterImageRequest;
import com.mirae.store.domain.store.controller.model.request.UpdateImageRequest;
import com.mirae.store.domain.store.controller.model.response.NearbyStoreResponse;
import com.mirae.store.domain.store.controller.model.request.LocationRequest;
import com.mirae.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.mirae.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.mirae.store.domain.store.controller.model.response.MessageResponse;
import com.mirae.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.mirae.store.domain.store.controller.model.response.StoreNameKeywordResponse;
import com.mirae.store.domain.store.controller.model.response.StoreRegisterResponse;
import com.mirae.store.domain.store.controller.model.response.StoreSimpleResponse;
import com.mirae.store.domain.store.controller.model.response.UserStoreResponse;
import com.mirae.store.domain.store.converter.MessageConverter;
import com.mirae.store.domain.store.converter.StoreConverter;
import com.mirae.store.domain.store.repository.Store;
import com.mirae.store.domain.store.repository.enums.ImageKind;
import com.mirae.store.domain.store.repository.enums.OperatingStatus;
import com.mirae.store.domain.store.repository.enums.StoreStatus;
import com.mirae.store.domain.store.service.StoreService;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class StoreBusiness {

  private final StoreService storeService;
  private final StoreConverter storeConverter;
  private final MessageConverter messageConverter;

  public StoreRegisterResponse register(StoreRegisterRequest storeRegisterRequest, Long userId) {

    Store store = storeConverter.toEntity(storeRegisterRequest, userId);

    store.register();
    storeService.save(store);

    RegisterImageRequest registerImageRequest = RegisterImageRequest.builder()
        .storeId(store.getId())
        .imageKind(ImageKind.STORE)
        .serverName(storeRegisterRequest.getServerName())
        .build();
    storeService.publishRegisterImage(registerImageRequest);

    return storeConverter.toResponse(store);
  }

  public MessageResponse unregister(Long storeId, Long userId) {
    Store targetStore = storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));

    targetStore.unregister();
    storeService.delete(targetStore);
    return messageConverter.toResponse("스토어가 삭제되었습니다.");

  }

  public MessageResponse update(Long storeId, StoreUpdateRequest storeUpdateRequest) {
    Store targetStore = storeService.getStoreById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));

    // 생각 중(수정)
    targetStore.updateName(storeUpdateRequest.getName());
    targetStore.updateAddress(storeUpdateRequest.getAddress());
    targetStore.updatePhone(storeUpdateRequest.getPhone());
    targetStore.updateCategory(storeUpdateRequest.getCategory());
    targetStore.updateOperatingTime(storeUpdateRequest.getOperatingTime().getOpeningTime(),
        storeUpdateRequest.getOperatingTime().getClosingTime());

    storeService.save(targetStore);

    UpdateImageRequest req = UpdateImageRequest.builder()
        .storeId(targetStore.getId())
        .imageKind(ImageKind.STORE)
        .serverName(storeUpdateRequest.getServerName())
        .build();
    storeService.publishUpdateImage(req);

    return messageConverter.toResponse("스토어 정보가 수정되었습니다.");
  }

  // 점주의 스토어 단건 조회
  public StoreSimpleResponse getSimpleStore(Long userId) {
    List<Store> stores = storeService.getStoresByUserId(userId);

    if (stores.isEmpty()) {
      throw new IllegalArgumentException("STORE NOT FOUND");
    }

    return storeConverter.toStoreSimpleResponse(stores);
  }

  // 점주의 스토어 리스트 조회
  public List<OwnerStoresResponse> getOwnerStore(Long userId) {
    List<Store> stores = storeService.getStoresByUserId(userId);
    if(stores.isEmpty()) {
      throw new IllegalArgumentException("STORE NOT FOUND");
    }

    return storeConverter.toOwnerStoresResponse(stores);
  }

  public Store getStoreByIdAndUserId(Long storeId, Long userId) {
    return storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));
  }

  // 고객이 조회하는 스토어
  public UserStoreResponse userStore(Long storeId) {
    Store store = storeService.getStoreById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));

    return storeConverter.toUserStoreResponse(store);
  }

  public List<StoreNameKeywordResponse> getStoresByNameKeyword(String name) {
    if (name.isBlank()) {
      throw new IsBlankException(StoreErrorCode.IS_BLANK);

    }

    List<Store> stores = storeService.getStoresByNameKeyword(name);
    if (stores.isEmpty()) {
      throw new IllegalArgumentException("STORE NOT FOUND");
    }

    return storeConverter.toStoreNameKeywordResponse(stores);

  }

  // 주소 근처 && 영업 중인 스토어
  public List<NearbyStoreResponse> getStoresByNearby(LocationRequest locationRequest) {
    List<Store> nearbyStores = storeService.getStoresByRegionAndRadius(
        locationRequest.getSido(), locationRequest.getSigungu(),
        locationRequest.getEupMyeonDong(), locationRequest.getLatitude(),
        locationRequest.getLongitude(), locationRequest.getRadiusInMeters());

    nearbyStores.stream().filter(
        store -> store.getStoreStatus() == StoreStatus.REGISTERED
        && store.getOperatingStatus() == OperatingStatus.OPEN
    ).collect(Collectors.toList());

    return storeConverter.toNearbyStoreResponse(nearbyStores, locationRequest.getRadiusInMeters());
  }

  public void updateStoreOpenClose() {
    LocalTime now = LocalTime.now();
    List<Store> stores = storeService.getStoreByDateTime(now);

    if (stores.isEmpty()) {
      throw new IllegalArgumentException("STORE_NOT_FOUND");
    }


  }
}
