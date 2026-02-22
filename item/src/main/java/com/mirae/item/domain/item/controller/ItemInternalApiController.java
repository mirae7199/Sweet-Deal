package com.mirae.item.domain.item.controller;

import com.mirae.item.domain.item.business.ItemBusiness;
import com.mirae.item.domain.item.controller.model.request.ItemInternalRequest;
import com.mirae.item.domain.item.controller.model.response.ItemInternalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class ItemInternalApiController {

  private final ItemBusiness itemBusiness;

  @PostMapping("/item")
  public ResponseEntity<ItemInternalResponse> getItem(@RequestBody ItemInternalRequest itemInternalRequest) {
    ItemInternalResponse itemInternalResponse = itemBusiness.getItemInternal(itemInternalRequest);
    return ResponseEntity.ok(itemInternalResponse);
  }


}
