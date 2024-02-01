package org.berka.manager;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "${feign.product}",decode404 = true,name = "category-product")
public interface IProductManager {

}
