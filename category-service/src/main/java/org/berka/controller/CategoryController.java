package org.berka.controller;

import lombok.RequiredArgsConstructor;
import org.berka.constant.EndPoints;
import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.repository.entity.Category;
import org.berka.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.berka.constant.EndPoints.*;

@RequestMapping(CATEGORY)
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping(CREATE)
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CreateRequestDto dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    @DeleteMapping(DELETE+"/{id}")
    public ResponseEntity<MessageResponseDto> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteCategory(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<MessageResponseDto> updateCategory(@Valid @RequestBody UpdateRequestDto dto) {
        return ResponseEntity.ok(service.updateCategory(dto));
    }

    @GetMapping(APPROVE)
    public ResponseEntity<Boolean> approveCategory(@RequestParam Long id) {
        return ResponseEntity.ok(service.approveCategory(id));
    }

    @GetMapping(GETNAME+"/{id}")
    public ResponseEntity<MessageResponseDto> getName(@PathVariable Long id) {
        return ResponseEntity.ok(service.getName(id));
    }

    @GetMapping(GETALL)
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(GETITSELF+"/{id}")
    public ResponseEntity<Category> getItself(@PathVariable Long id) {
        return ResponseEntity.ok(service.getItself(id));
    }

}
