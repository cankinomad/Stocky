package org.berka.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.berka.constant.EndPoint.APPROVE;

@FeignClient(url = "${feign.storage}",decode404 = true,name = "product-storage")
public interface IStorageManager {

    @GetMapping(APPROVE)
    ResponseEntity<Boolean> approveStorage(@RequestParam Long id);
}
