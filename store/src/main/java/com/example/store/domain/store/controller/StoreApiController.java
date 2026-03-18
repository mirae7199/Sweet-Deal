package com.example.store.domain.store.controller;

import com.example.global.annotation.UserSession;
import com.example.global.resolver.User;
import com.example.store.domain.store.business.StoreBusiness;
import com.example.store.domain.store.controller.model.request.LocationRequest;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.example.store.domain.store.controller.model.response.NearbyStoresResponse;
import com.example.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.example.store.domain.store.controller.model.response.StoresNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.UserStoreResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreApiController {

    public final StoreBusiness storeBusiness;

    @PostMapping // 200
    public ResponseEntity<Void> register(
        @RequestBody @Valid StoreRegisterRequest storeRegisterRequest,
        @Parameter(hidden = true) @UserSession User user
    ) {

        storeBusiness.register(storeRegisterRequest, user.getId());

        log.info("=========store register=========");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{storeId}") // 200
    public ResponseEntity<Void> unregister(
        @PathVariable Long storeId,
        @Parameter(hidden = true) @UserSession User user
    ) {
        storeBusiness.unregister(storeId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{storeId}") // 200
    public ResponseEntity<Void> update(
        @PathVariable Long storeId,
        @RequestBody @Valid StoreUpdateRequest storeUpdateRequest
    ) {
        storeBusiness.update(storeId, storeUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner") // 200
    public ResponseEntity<OwnerStoresResponse> ownerStores(
        @Parameter(hidden = true) @UserSession User user
    ) {
        OwnerStoresResponse ownerStores = storeBusiness.getOwnerStore(user.getId());
        return ResponseEntity.ok(ownerStores);
    }

    @GetMapping("/{storeId}") // 200
    public ResponseEntity<UserStoreResponse> userStore(
        @PathVariable Long storeId
    ) {
        UserStoreResponse userStoreResponse = storeBusiness.userFindStore(storeId);
        return ResponseEntity.ok(userStoreResponse);
    }

    @GetMapping("/nearby")
    public ResponseEntity<NearbyStoresResponse> nearby(
        @PathVariable @Valid LocationRequest locationRequest
    ) {
        NearbyStoresResponse storesByNearby = storeBusiness.getStoresByNearby(locationRequest);
        return ResponseEntity.ok((storesByNearby));
    }

    @GetMapping("/search") // 200
    public ResponseEntity<StoresNameKeywordResponse> nameKeyword(
        @RequestParam String name
    ) {
        StoresNameKeywordResponse storesByNameKeyword = storeBusiness.getStoresByNameKeyword(
            name);

        return ResponseEntity.ok(storesByNameKeyword);
    }

}
