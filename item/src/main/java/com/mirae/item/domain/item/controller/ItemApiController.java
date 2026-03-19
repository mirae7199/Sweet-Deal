package com.mirae.item.domain.item.controller;


import com.mirae.global.annotation.UserSession;
import com.mirae.global.resolver.User;
import com.mirae.item.domain.item.controller.model.response.ItemDetailResponse;
import com.mirae.item.domain.item.controller.model.response.ItemsResponse;
import com.mirae.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.mirae.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.mirae.item.domain.item.business.ItemBusiness;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemApiController {

    private final ItemBusiness itemBusiness;

    @PostMapping()
    public ResponseEntity<Void> register(
        @Parameter(hidden = true) @UserSession User user,
        @RequestBody @Valid ItemRegisterRequest request
    ) {
        itemBusiness.register(user.getId(), request);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> unregister(
        @Parameter(hidden = true) @UserSession User user,
        @PathVariable Long itemId
        ) {
        itemBusiness.unregister(user.getId(), itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Void> update(
        @Parameter(hidden = true) @UserSession User user,
        @PathVariable Long itemId,
        @RequestBody ItemUpdateRequest itemUpdateRequest
        ) {

        itemBusiness.update(user.getId(), itemId, itemUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDetailResponse> getItem(
        @Parameter(hidden = true) @UserSession User user,
        @PathVariable Long itemId
        ) {
        ItemDetailResponse itemDetailResponse = itemBusiness.getItemBy(user.getId(), itemId);
        return ResponseEntity.ok(itemDetailResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<ItemsResponse> getItemList(
        @Parameter(hidden = true) @UserSession User user
    ) {

        ItemsResponse itemsResponse = itemBusiness.getItemsBy(user.getId());
        return ResponseEntity.ok(itemsResponse);
    }

    // 상품 상세 조회 (고객)
    @GetMapping("/public/{itemId}")
    public ResponseEntity<ItemDetailResponse> getItemForCustomer(
        @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(itemBusiness.getItemForCustomer(itemId));
    }

    // 특정 스토어 상품 목록 조회 (고객)
    @GetMapping("/public/store/{storeId}")
    public ResponseEntity<ItemsResponse> getItemsForCustomer(
        @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(itemBusiness.getItemsForCustomer(storeId));
    }
}