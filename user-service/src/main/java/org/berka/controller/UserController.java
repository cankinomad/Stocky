package org.berka.controller;

import lombok.RequiredArgsConstructor;
import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.dto.response.UserResponseDto;
import org.berka.rabbitmq.model.RegisterUserModel;
import org.berka.repository.entity.User;
import org.berka.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.berka.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserController {

    private final UserService service;

    @PostMapping(REGISTER)
    public ResponseEntity<User> registerUser(@RequestBody RegisterUserModel model) {
        return ResponseEntity.ok(service.registerUser(model));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<MessageResponseDto> updateUser(@Valid @RequestBody UpdateRequestDto dto) {
        return ResponseEntity.ok(service.updateUser(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<MessageResponseDto> deleteUser(@RequestParam String  token) {
        return ResponseEntity.ok(service.deleteUser(token));
    }

    @PutMapping(CHANGEPASSWORD)
    public ResponseEntity<MessageResponseDto> changePassword(@Valid @RequestBody ChangePasswordRequestDto dto) {
        return ResponseEntity.ok(service.changePassword(dto));
    }

    @GetMapping(USERINFORMATION+"/{token}")
    public ResponseEntity<UserResponseDto> userInformation(@PathVariable String token){
        return ResponseEntity.ok(service.userInformation(token));
    }

}
