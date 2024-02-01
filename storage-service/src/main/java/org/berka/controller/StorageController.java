package org.berka.controller;

import lombok.RequiredArgsConstructor;
import org.berka.constant.EndPoints;
import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.repository.entity.Storage;
import org.berka.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.function.BinaryOperator;

import static org.berka.constant.EndPoints.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(STORAGE)
public class StorageController {

    private final StorageService service;


    @PostMapping(CREATE)
    public ResponseEntity<Storage> createStorage(@Valid @RequestBody CreateRequestDto dto) {
        return ResponseEntity.ok(service.createStorage(dto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<MessageResponseDto> updateStorage(@Valid @RequestBody UpdateRequestDto dto) {
        return ResponseEntity.ok(service.updateStorage(dto));
    }

    @DeleteMapping(DELETE+"/{id}")
    public ResponseEntity<MessageResponseDto> deleteStorage(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteStorage(id));
    }

    @GetMapping(APPROVE)
    public ResponseEntity<Boolean> approveStorage(@RequestParam Long id) {
        return ResponseEntity.ok(service.approveStorage(id));
    }

    @GetMapping(GETNAME+"/{id}")
    public ResponseEntity<MessageResponseDto> getName(@PathVariable Long id) {
        return ResponseEntity.ok(service.getName(id));
    }

    @GetMapping(GETALL)
    public ResponseEntity<List<Storage>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

}
