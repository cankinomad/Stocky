package org.berka.controller;

import lombok.RequiredArgsConstructor;
import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.repository.entity.Unit;
import org.berka.service.UnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.List;

import static org.berka.constant.EndPoints.*;

@RestController
@RequestMapping(UNIT)
@RequiredArgsConstructor
public class UnitController {

    private final UnitService service;

    @PostMapping(CREATE)
    public ResponseEntity<Unit> createUnit(@Valid @RequestBody CreateRequestDto dto) {
        return ResponseEntity.ok(service.createUnit(dto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<MessageResponseDto> updateUnit(@Valid @RequestBody UpdateRequestDto dto) {
        return ResponseEntity.ok(service.updateUnit(dto));
    }

    @DeleteMapping(DELETE+"/{id}")
    public ResponseEntity<MessageResponseDto> deleteUnit(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteUnit(id));
    }

    @GetMapping(APPROVE)
    public ResponseEntity<Boolean> approveUnit(@RequestParam Long id) {
        return ResponseEntity.ok(service.approveUnit(id));
    }

    @GetMapping(GETNAME + "/{id}")
    public ResponseEntity<MessageResponseDto> getName(@PathVariable Long id) {
        return ResponseEntity.ok(service.getName(id));
    }

    @GetMapping(GETALL)
    public ResponseEntity<List<Unit>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
