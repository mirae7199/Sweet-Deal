package com.example.store.domain.store.controller.model.request;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.OperatingTime;
import com.example.store.domain.store.repository.enums.StoreCategory;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record StoreUpdateRequest(
    @Pattern(
        regexp = "^[가-힣A-Za-z0-9 ]{1,100}$",
        message = "한글, 영문, 숫자 및 공백만 입력 가능합니다."
    )
    String name,
    Address address,
    @Size(min = 11, max = 11, message = "전화번호는 숫자 11자리여야 합니다.")
    String phone,
    StoreCategory category,
    OperatingTime operatingTime,
    List<String> serverName
) {


}
