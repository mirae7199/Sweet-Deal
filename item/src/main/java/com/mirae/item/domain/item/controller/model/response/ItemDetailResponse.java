package com.mirae.item.domain.item.controller.model.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ItemDetailResponse(

   Long itemId,
   String name,
   Integer quantity,
   LocalDateTime expiredAt,
   Integer originalPrice,
   Integer discountPrice,
   Long storeId
) {


}
