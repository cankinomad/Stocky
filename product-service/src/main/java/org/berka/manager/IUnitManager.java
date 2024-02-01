package org.berka.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.berka.constant.EndPoint.APPROVE;

@FeignClient(url = "${feign.unit}",decode404 = true,name = "product-unit")
public interface IUnitManager {

    @GetMapping(APPROVE)
    ResponseEntity<Boolean> approveUnit(@RequestParam Long id);
}
