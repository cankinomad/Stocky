package org.berka.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.request.LoginRequestDto;
import org.berka.dto.request.RegisterRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.dto.response.TokenResponseDto;
import org.berka.repository.entity.Auth;
import org.berka.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.berka.constant.EndPoints.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping(REGISTER)
    public ResponseEntity<MessageResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto dto) {
        return ResponseEntity.ok(service.registerUser(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(service.login(dto));
    }
    @Hidden
    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> deleteUser(@RequestParam String token) {
        return ResponseEntity.ok(service.deleteUser(token));
    }

    @PutMapping(CHANGEPASSWORD)
    public ResponseEntity<MessageResponseDto> changePassword(@RequestBody ChangePasswordRequestDto dto) {
        return ResponseEntity.ok(service.changePassword(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> getAllAuth() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(TOKENVALIDATION+"/{token}")
    public ResponseEntity<Boolean> isTokenValidate(@PathVariable String token){
        return ResponseEntity.ok(service.tokenValidation(token));
    }
}
