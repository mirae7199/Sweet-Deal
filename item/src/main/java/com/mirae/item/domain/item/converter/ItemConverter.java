package com.mirae.item.domain.item.converter;

import com.mirae.global.annotation.Converter;
import com.mirae.item.domain.item.controller.model.response.ItemDetailResponse;
import com.mirae.item.domain.item.controller.model.response.ItemInform;
import com.mirae.item.domain.item.controller.model.response.ItemInternalResponse;
import com.mirae.item.domain.item.controller.model.response.ItemResponse;
import com.mirae.item.domain.item.controller.model.response.ItemsResponse;
import com.mirae.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.mirae.item.domain.item.entity.Item;
import java.util.List;

@Converter
public class ItemConverter {

  public Item toEntity(ItemRegisterRequest req, Long storeId) {
        return Item.builder()
            .name(req.name())
            .quantity(req.quantity())
            .expiredAt(req.expiredAt())
            .originalPrice(req.originalPrice())
            .discountPrice(req.discountPrice())
            .storeId(storeId)
            .build();
  }

  public ItemsResponse toItemsResponse(List<Item> items) {
    List<ItemResponse> itemResponses = items.stream().map(item -> {
      return ItemResponse.builder()
          .itemId(item.getId())
          .name(item.getName())
          .expiredAt(item.getExpiredAt())
          .quantity(item.getQuantity())
          .originalPrice(item.getOriginalPrice())
          .discountPrice(item.getDiscountPrice())
          .build();
    }).toList();

    return ItemsResponse.builder()
        .itemResponses(itemResponses)
        .build();
  }

  public ItemDetailResponse toDetailResponse(Item item) {
    return ItemDetailResponse.builder()
        .itemId(item.getId())
        .name(item.getName())
        .quantity(item.getQuantity())
        .expiredAt(item.getExpiredAt())
        .originalPrice(item.getOriginalPrice())
        .discountPrice(item.getDiscountPrice())
        .storeId(item.getStoreId())
        .build();
  }

  public ItemInternalResponse toInternalResponse(List<Item> items) {

    return ItemInternalResponse.builder()
        .itemInforms(
            items.stream().map(
                item ->{
                     return ItemInform.builder()
                        .itemId(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .expiredAt(item.getExpiredAt())
                        .registerAt(item.getRegisteredAt())
                        .status(item.getStatus())
                        .originalPrice(item.getOriginalPrice())
                        .discountPrice(item.getDiscountPrice())
                        .storeId(item.getStoreId())
                        .build();
                }
            ).toList()).build();
  }
}
