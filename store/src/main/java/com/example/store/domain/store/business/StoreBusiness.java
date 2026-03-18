package com.example.store.domain.store.business;

import com.example.global.annotation.Business;
import com.example.global.errorcode.StoreErrorCode;
import com.example.global.exception.BusinessException;
import com.example.store.domain.store.controller.model.request.RegisterImageRequest;
import com.example.store.domain.store.controller.model.request.UpdateImageRequest;
import com.example.store.domain.store.controller.model.response.NearbyStoresResponse;
import com.example.store.domain.store.controller.model.request.LocationRequest;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.example.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.example.store.domain.store.controller.model.response.StoreSimpleResponse;
import com.example.store.domain.store.controller.model.response.StoresNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.UserStoreResponse;
import com.example.store.domain.store.converter.StoreConverter;
import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.repository.enums.ImageKind;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreStatus;
import com.example.store.domain.store.service.StoreService;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreBusiness {

  private final StoreService storeService;
  private final StoreConverter storeConverter;

  /**
   *
   * @param storeRegisterRequest
   * @param userId
   * @return StoreRegisterResponse
   * 스토어 등록
   */
  @Transactional(readOnly = false)
  public void register(StoreRegisterRequest storeRegisterRequest, Long userId) {

    Store store = storeConverter.toEntity(storeRegisterRequest, userId);

    store.register();
    storeService.save(store);

    RegisterImageRequest registerImageRequest =
        RegisterImageRequest.of(
            null,
            store.getId(),
            ImageKind.STORE,
            storeRegisterRequest.getServerName()
    );

    storeService.publishRegisterImage(registerImageRequest);
  }

  /**
   *
   * @param storeId
   * @param userId
   * 스토어 삭제
   */
  public void unregister(Long storeId, Long userId) {
    Store targetStore = storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));

    targetStore.unregister();
    storeService.delete(targetStore);
  }

  /**
   *
   * @param storeId
   * @param storeUpdateRequest
   * 스토어 수정
   */
  public void update(Long storeId, StoreUpdateRequest storeUpdateRequest) {
    Store targetStore = storeService.getStoreById(storeId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));

    // 생각 중(수정)
    targetStore.update(
        storeUpdateRequest.name(),
        storeUpdateRequest.address(),
        storeUpdateRequest.phone(),
        storeUpdateRequest.category(),
        storeUpdateRequest.operatingTime().getOpeningTime(),
        storeUpdateRequest.operatingTime().getClosingTime()
    );

    storeService.save(targetStore);

    UpdateImageRequest updateImageRequest = UpdateImageRequest.of(
        null,
        targetStore.getId(),
        ImageKind.STORE,
        storeUpdateRequest.serverName()
    );

    storeService.publishUpdateImage(updateImageRequest);
  }

  /**
   *
   * @param userId
   * @return StoreSimpleResponse
   * 외부 모듈에서 스토어 조회
   */
  public StoreSimpleResponse getSimpleStore(Long userId) {
    List<Store> stores = storeService.getStoresByUserId(userId);

    if (stores.isEmpty()) {
      throw new BusinessException(StoreErrorCode.STORE_NOT_FOUND);
    }

    return storeConverter.toStoreSimpleResponse(stores);
  }

  /**
   *
   * @param userId
   * @return OwnerStoresResponse
   * 점주의 스토어 리스트 조회
   */
  public OwnerStoresResponse getOwnerStore(Long userId) {
    List<Store> stores = storeService.getStoresByUserId(userId);
    if(stores.isEmpty()) {
      throw new BusinessException(StoreErrorCode.STORE_NOT_FOUND);
    }

    return storeConverter.toOwnerStoresResponse(stores);
  }

  public Store getStoreByIdAndUserId(Long storeId, Long userId) {
    return storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));
  }

  /**
   *
   * @param storeId
   * @return UserStoreResponse
   * 고객이 조회하는 스토어
   */
  public UserStoreResponse userFindStore(Long storeId) {
    Store store = storeService.getStoreById(storeId)
        .orElseThrow(() -> new BusinessException(StoreErrorCode.STORE_NOT_FOUND));

    return storeConverter.toUserStoreResponse(store);
  }

  /**
   *
   * @param name
   * @return StoresNameKeywordResponse
   * 스토어 키워드 검색
   */
  public StoresNameKeywordResponse getStoresByNameKeyword(String name) {
    if (name.isBlank()) {
      throw new BusinessException(StoreErrorCode.IS_BLANK);
    }

    List<Store> stores = storeService.getStoresByNameKeyword(name);
    if (stores.isEmpty()) {
      throw new BusinessException(StoreErrorCode.STORE_NOT_FOUND);
    }

    return storeConverter.toStoreNameKeywordResponse(stores);

  }

  /**
   *
   * @param locationRequest
   * @return NearbyStoresResponse
   * 주소 근처 && 영업 중인 스토어 조회
   */
  public NearbyStoresResponse getStoresByNearby(LocationRequest locationRequest) {
    List<Store> nearbyStores = storeService.getStoresByRegionAndRadius(
        locationRequest.getSido(), locationRequest.getSigungu(),
        locationRequest.getEupMyeonDong(), locationRequest.getLatitude(),
        locationRequest.getLongitude(), locationRequest.getRadiusInMeters());

    List<Store> openingNearByStore = nearbyStores.stream().filter(
        store -> store.getStoreStatus() == StoreStatus.REGISTERED
            && store.getOperatingStatus() == OperatingStatus.OPEN
    ).toList();

    return storeConverter.toNearbyStoreResponse(openingNearByStore, locationRequest.getRadiusInMeters());
  }

  public void updateStoreOpenClose() {
    LocalTime now = LocalTime.now();
    List<Store> stores = storeService.getStoreByDateTime(now);

    if (stores.isEmpty()) {
      throw new BusinessException(StoreErrorCode.STORE_NOT_FOUND);
    }

  }
}
