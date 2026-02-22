package com.mirae.item.domain.item.controller;


import com.mirae.global.anntation.UserSession;
import com.mirae.global.resolver.User;
import com.mirae.item.domain.item.controller.model.response.MessageResponse;
import com.mirae.item.domain.item.controller.model.request.ItemDeleteRequest;
import com.mirae.item.domain.item.controller.model.response.ItemDetailResponse;
import com.mirae.item.domain.item.controller.model.response.ItemListResponse;
import com.mirae.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.mirae.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.mirae.item.domain.item.business.ItemBusiness;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
        @RequestBody @Valid ItemRegisterRequest request)
        // @Parameter(hidden = true) @UserSession User user)
    {
            Long userId = 1L;

        MessageResponse response = itemBusiness.register(request, userId);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/unregister")
    public ResponseEntity<MessageResponse> unregister(
        @RequestBody ItemDeleteRequest itemDeleteRequest)
        // @Parameter(hidden = true) @UserSession User user)
    {

        Long userId = 1L;

        MessageResponse messageResponse = itemBusiness.unregister(itemDeleteRequest, userId);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/{itemId}/update")
    public ResponseEntity<MessageResponse> update(
        @PathVariable Long itemId,
        @RequestBody ItemUpdateRequest itemUpdateRequest)
        // @Parameter(hidden = true) @UserSession User user) {
    {
        Long userId = 1L;

        MessageResponse messageResponse = itemBusiness.update(itemId, itemUpdateRequest,
            userId);
        return ResponseEntity.ok(messageResponse);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDetailResponse> getItem(
        @PathVariable Long itemId)
        // @Parameter(hidden = true) @UserSession User user) {
    {
        Long userId = 1L;

        ItemDetailResponse itemDetailResponse = itemBusiness.getItemBy(itemId, userId);
        return ResponseEntity.ok(itemDetailResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ItemListResponse>> getItemList()
            // @Parameter(hidden = true) @UserSession User user) {
    {

        Long userId = 1L;

        List<ItemListResponse> itemList = itemBusiness.getItemListBy(userId);
        return ResponseEntity.ok(itemList);
    }
}