package com.mirae.item.domain.item.business;

import com.mirae.global.annotation.Business;
import com.mirae.global.errorcode.ItemErrorCode;
import com.mirae.global.errorcode.StoreErrorCode;
import com.mirae.global.exception.BusinessException;
import com.mirae.item.domain.item.controller.model.request.MessageCancelRequest;
import com.mirae.item.domain.item.controller.model.request.MessageUpdateRequest;
import com.mirae.item.domain.item.controller.model.request.OrderItemRequest;
import com.mirae.item.domain.item.controller.model.request.RegisterImageRequest;
import com.mirae.item.domain.item.controller.model.request.UpdateImageRequest;
import com.mirae.item.domain.item.controller.model.response.StoreSimpleResponse;
import com.mirae.item.domain.item.controller.model.request.ItemInternalRequest;
import com.mirae.item.domain.item.controller.model.response.ItemDetailResponse;
import com.mirae.item.domain.item.controller.model.response.ItemInternalResponse;
import com.mirae.item.domain.item.controller.model.response.ItemsResponse;
import com.mirae.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.mirae.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.mirae.item.domain.item.converter.ItemConverter;
import com.mirae.item.domain.item.entity.Item;
import com.mirae.item.domain.item.entity.enums.ImageKind;
import com.mirae.item.domain.item.entity.enums.ItemStatus;
import com.mirae.item.domain.item.service.ItemService;
import com.mirae.item.domain.item.service.StoreFeignClient;
import feign.FeignException.FeignClientException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Business
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ItemBusiness {

    private final ItemService itemService;
    private final ItemConverter itemConverter;
    private final StoreFeignClient storeFeignClient;

    /**
     *
     * @param request
     * @param userId
     * 선택한 스토어에 상품 등록
     */
    public void register(Long userId, ItemRegisterRequest request) {

        List<Long> storeIds = getStoreIds(userId);
        Long storeId = request.storeId();

        validateStoreOwnership(storeIds, storeId);

        Item registerItem = itemConverter.toEntity(request, storeId);

        // 중복 상품 검증
        itemService.existsByItemWithThrow(storeId, request.name(), request.expiredAt(),
            List.of(ItemStatus.SALE, ItemStatus.RESERVED));

        // 검증
        itemService.validateRegister(registerItem);
        Item item = itemService.register(registerItem);

        RegisterImageRequest registerImageRequest = RegisterImageRequest.of(
            item.getId(),
            storeId,
            ImageKind.ITEM,
            request.serverNames()
        );

        itemService.publishRegisterImage(registerImageRequest);
    }

    /**
     *
     * @param userId
     * @param itemId
     * 상품 삭제
     */
    public void unregister(Long userId, Long itemId) {

        List<Long> storeIds = getStoreIds(userId);

        // 상품이 존재하지 않을 시 예외 발생
        // 판매된 상품과 삭제된 상품은 제외 (SOLD, DELETED)
        itemService.notExistsByItemWithThrow(itemId, storeIds);

        Item targetItem = itemService.getItemByIdAndStatusList(itemId,
            List.of(ItemStatus.SALE, ItemStatus.RESERVED));

        validateStoreOwnership(storeIds, targetItem.getStoreId());

        itemService.unregister(targetItem);

    }

    /*
    판매된 상품은 30일 보관후 자동으로 삭제한다.
    (SOLD -> DELETED 변경)
    스케줄러에서 사용
     */
    public void deleteExpiredSoldItems() {

        LocalDateTime expiredDate = LocalDateTime.now().minusDays(30);

        itemService.deleteSoldItemAfter30Days(expiredDate);
    }

    /*
    유통기한이 지났는지 확인 후 삭제 상태로 변경 스케줄러 사용
    */
    public void deleteExpiredAtOver() {
        LocalDateTime present = LocalDateTime.now();

        itemService.expireItemsToDeleted(present);
    }

    /**
     *
     * @param userId
     * @param itemId
     * @param request
     * 상품 수정
     */
    public void update(Long userId, Long itemId, ItemUpdateRequest request) {

        List<Long> storeIds = getStoreIds(userId);
        Long storeId = request.storeId();

        validateStoreOwnership(storeIds, storeId);

        // 상품이 존재하지 않으면 예외 발생
        itemService.notExistsByItemWithThrow(itemId, storeIds);

        Item targetItem = itemService.getItemById(itemId);

        itemService.update(targetItem, request);

        UpdateImageRequest req = UpdateImageRequest.of(
            itemId,
            storeId,
            ImageKind.ITEM,
            request.serverName()
            );

        itemService.publishUpdateImage(req);
    }

    /**
     *
     * @param userId
     * @param itemId
     * @return ItemDetailResponse
     * 상품 상세 조회 (스토어 매니저)
     */
    @Transactional(readOnly = true)
    public ItemDetailResponse getItemBy(Long userId, Long itemId) {

        List<Long> storeIds = getStoreIds(userId);

        // 해당 스토어에 특정 상품이 없을 시 예외 발생
        itemService.notExistsByItemWithThrow(itemId, storeIds);
        Item item = itemService.getItemById(itemId);

        return itemConverter.toDetailResponse(item);

    }

    /**
     *
     * @param userId
     * @return ItemsResponse
     * 상품 목록 조회 (스토어 매니저)
     */
    @Transactional(readOnly = true)
    public ItemsResponse getItemsBy(Long userId) {

        List<Long> storeIds = getStoreIds(userId);

        // storeId에 해당하는 SALE중인 상품 목록 조회
        List<Item> items = itemService.getItemListByStoresIdAndStatus(storeIds,
            ItemStatus.SALE);

        return itemConverter.toItemsResponse(items);
    }

    /**
     *
     * @param itemInternalRequest
     * @return ItemInternalResponse
     * 외부 모듈에서 조회용
     */
    @Transactional(readOnly = true)
    public ItemInternalResponse getItemInternal(ItemInternalRequest itemInternalRequest) {
        List<Long> itemIds = itemInternalRequest.itemIds().stream().toList();

        List<Item> items = itemIds.stream()
            .map(itemService::getItemById)
            .toList();

        return itemConverter.toInternalResponse(items);
    }

    /**
     *
     * @param itemId
     * @return ItemDetailResponse
     * 상품 상세 조회 (고객)
     */
    @Transactional(readOnly = true)
    public ItemDetailResponse getItemForCustomer(Long itemId) {
        Item item = itemService.getItemByIdAndStatus(itemId, ItemStatus.SALE);
        return itemConverter.toDetailResponse(item);
    }

    /**
     *
     * @param storeId
     * @return ItemsResponse
     * 특정 스토어 상품 목록 조회 (고객)
     */
    @Transactional(readOnly = true)
    public ItemsResponse getItemsForCustomer(Long storeId) {
        List<Item> items = itemService.getItemListByStoresIdAndStatus(
            List.of(storeId), ItemStatus.SALE);
        return itemConverter.toItemsResponse(items);
    }

    private static void validateStoreOwnership(List<Long> storeIds, Long storeId) {
        // storeIds가 빈 List일 때
        if(storeIds.isEmpty()) {
            throw new BusinessException(StoreErrorCode.STORE_NOT_FOUND);
        }

        // 내가 선택한 스토어와 유저 정보로 조회한 스토어 정보가 다르면.
        if (!storeIds.contains(storeId)) {
            throw new BusinessException(ItemErrorCode.STORE_NOT_OWNED);
        }
    }

    private List<Long> getStoreIds(Long userId) {
        try {
            return Optional.ofNullable(storeFeignClient.getStores(userId).getBody())
                .map(StoreSimpleResponse::storesId)
                .orElseThrow(() -> new BusinessException(ItemErrorCode.STORE_SERVICE_ERROR));
        } catch (FeignClientException e) {
            throw new BusinessException(ItemErrorCode.STORE_SERVICE_ERROR);
        }
    }

    /**
     *
     * @param messageUpdateRequest
     * @param partition
     * @param offset
     * 주문 메시지 (주문에 따른 재고 감소)
     */
    @Transactional(readOnly = false)
    @KafkaListener(topics = "item.update", groupId = "item-group") // 200
    public void handlerUpdateItem(@Payload MessageUpdateRequest messageUpdateRequest,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset) {

        List<Item> lockedItems;

        try {

            log.info("Received order: {}, partition: {}, offset: {}",
                messageUpdateRequest.orderId(),
                partition, offset);

            List<OrderItemRequest> orderItemRequests = messageUpdateRequest.orderItemRequests();

            // 재고 검증만 먼저 실행
            lockedItems = orderItemRequests.stream().map(orderItemRequest -> {
                // 비관적 락
                Item item = itemService.getItemByIdPessimisticLock(
                    orderItemRequest.itemId());

                if (item.getQuantity() < orderItemRequest.quantity()) {
                    itemService.publishCancelOrder(messageUpdateRequest);
                    throw new BusinessException(ItemErrorCode.ITEM_OUT_OF_STOCK);
                }
                return item; // 검증된 상품.
            }).toList();

           for (int i=0; i < orderItemRequests.size(); i++) {
               Item item = lockedItems.get(i);

               item.insertOrderId(messageUpdateRequest.orderId());
               item.decreaseStock(orderItemRequests.get(i).quantity());

               if (item.getQuantity() == 0) {
                   item.changeStatus(ItemStatus.SOLD);
               }
               itemService.save(item);
               log.info("Updated item id: {}, remaining quantity: {}", item.getId(), item.getQuantity()); // 추가
           }


        } catch (Exception e) {
            log.error("Error processing order: {}", messageUpdateRequest.orderId(), e);
            throw e;
        }

    }

    /**
     *
     * @param messageCancelRequest
     * @param partition
     * @param offset
     * 주문 취소 메시지 (주문 취소에 따른 재고 증가)
     */
    @Transactional
    @KafkaListener(topics = "item.cancel", groupId = "item-group") // 200
    public void handlerCancelItem(@Payload MessageCancelRequest messageCancelRequest,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset) {

        List<Item> lockedItems;

        log.info("Received order: {}, partition: {}, offset: {}",
            messageCancelRequest.orderId(),
            partition, offset);

        lockedItems = messageCancelRequest.orderItemRequests().stream()
            .map(orderItemRequest -> {
                // 비관적 락
                Item item = itemService.getItemByIdPessimisticLock(orderItemRequest.itemId());

                if (item.getStatus() == ItemStatus.SOLD) {
                    item.changeStatus(ItemStatus.SALE);
                }

                // 취소한 재고 다시 증가.
                item.cancelStock(orderItemRequest.quantity());

                return item;
            }).toList();

        itemService.saveAll(lockedItems);
    }

}
