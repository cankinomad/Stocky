package org.berka.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.berka.constant.EndPoint.APPROVE;

@FeignClient(url = "${feign.category}",decode404 = true,name = "product-category")
public interface ICategoryManager {

    @GetMapping(APPROVE)
    ResponseEntity<Boolean> approveCategory(@RequestParam Long id);
}
