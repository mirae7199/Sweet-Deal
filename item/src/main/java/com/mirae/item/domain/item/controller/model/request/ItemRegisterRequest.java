package com.mirae.item.domain.item.controller.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.time.LocalDateTime;

public record ItemRegisterRequest(
    @NotNull
    @Pattern(
            regexp = "^[가-힣A-Za-z0-9 ]{1,100}$",
            message = "한글, 영문, 숫자 및 공백만 입력 가능합니다."
    )
    String name,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "지정된 일시는 현재 또는 미래여야 합니다.")
    LocalDateTime expiredAt,
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    Integer originalPrice,
    Integer discountPrice,
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    Integer quantity,
    @NotNull
    Long storeId,
    @NotNull
    List<String> serverNames
    ) {


}
