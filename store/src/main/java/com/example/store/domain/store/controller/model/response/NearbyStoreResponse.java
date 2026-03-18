package com.example.store.domain.store.controller.model.response;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record NearbyStoreResponse(
    Long id,
    String sido,
    String sigungu,
    String eupMyeonDong,
    BigDecimal latitude,
    BigDecimal longitude,
    BigDecimal radiusInMeters
) {

}

