package org.berka.manager;

import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.berka.constant.EndPoints.CHANGEPASSWORD;

@FeignClient(url = "${feign.auth}",decode404 = true,name = "user-auth")
public interface IAuthManager {

    @PutMapping(CHANGEPASSWORD)
    ResponseEntity<MessageResponseDto> changePassword(@RequestBody ChangePasswordRequestDto dto);
}
