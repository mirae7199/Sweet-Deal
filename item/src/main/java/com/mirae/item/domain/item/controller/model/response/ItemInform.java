package com.mirae.item.domain.item.controller.model.response;

import com.mirae.item.domain.item.entity.enums.ItemStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInform {
  private Long itemId;
  private String name;
  private Integer quantity;
  private LocalDateTime expiredAt;
  private LocalDateTime registerAt;
  private ItemStatus status;
  private Integer originalPrice;
  private Integer discountPrice;
  private Long storeId;

}
