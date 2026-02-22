package com.mirae.order.domain.order.service.model.item;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemInternalRequest {

  private List<Long> ItemIds;

}
