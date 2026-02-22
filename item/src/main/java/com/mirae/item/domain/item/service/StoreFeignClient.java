package com.mirae.item.domain.item.service;

import com.mirae.item.domain.item.controller.model.response.StoreSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store", path = "/internal")
public interface StoreFeignClient {

  @GetMapping(value = "/store/{userId}", headers = "X-Internal=true")
  ResponseEntity<StoreSimpleResponse> getStores(@PathVariable Long userId);
}
