package org.berka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/authservice")
    public ResponseEntity<String> authServiceFallback(){
        return ResponseEntity.ok("Auth service suanda hizmet verememektedir!!!");
    }

    @GetMapping("/userservice")
    public ResponseEntity<String> userServiceFallback(){
        return ResponseEntity.ok("User service suanda hizmet verememektedir!!!");
    }

    @GetMapping("/productservice")
    public ResponseEntity<String> productServiceFallback(){
        return ResponseEntity.ok("Product service suanda hizmet verememektedir!!!");
    }
    @GetMapping("/categoryservice")
    public ResponseEntity<String> categoryServiceFallback(){
        return ResponseEntity.ok("Category service suanda hizmet verememektedir!!!");
    }

    @GetMapping("/storageservice")
    public ResponseEntity<String> storageServiceFallback(){
        return ResponseEntity.ok("Storage service suanda hizmet verememektedir!!!");
    }
    @GetMapping("/unitservice")
    public ResponseEntity<String> unitServiceFallback(){
        return ResponseEntity.ok("Unit service suanda hizmet verememektedir!!!");
    }
}
