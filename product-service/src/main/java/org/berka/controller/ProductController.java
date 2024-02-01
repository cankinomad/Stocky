package org.berka.controller;

import lombok.RequiredArgsConstructor;
import org.berka.constant.EndPoint;
import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.repository.entity.Product;
import org.berka.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.berka.constant.EndPoint.*;

@RestController
@RequestMapping(PRODUCT)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;


    @PostMapping(CREATE)
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateRequestDto dto) {
        return ResponseEntity.ok(service.createProduct(dto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<MessageResponseDto> updateProduct(@Valid @RequestBody UpdateRequestDto dto) {
        return ResponseEntity.ok(service.updateProduct(dto));
    }

    @DeleteMapping(DELETE+"/{id}")
    public ResponseEntity<MessageResponseDto> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteProduct(id));
    }

    @GetMapping(ALLPRODUCTS)
    public ResponseEntity<List<Product>> allProducts(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(PRODUCTBYSTORAGE+"/{storageId}")
    public ResponseEntity<List<Product>> productByStorage(@PathVariable Long storageId){
        return ResponseEntity.ok(service.findByStorageId(storageId));
    }

    @GetMapping(PRODUCTBYCATEGORY+"/{categoryId}")
    public ResponseEntity<List<Product>> productByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(service.findByCategoryId(categoryId));
    }

    @PutMapping(CHANGECATEGORYTODEFAULT)
    public ResponseEntity<Boolean> changeCategoryToDefault(@RequestParam Long categoryId) {
        return ResponseEntity.ok(service.changeCategoryToDefault(categoryId));
    }

    @PutMapping(CHANGESTORAGETODEFAULT)
    public ResponseEntity<Boolean> changeStorageToDefault(@RequestParam Long storageId) {
        return ResponseEntity.ok(service.changeStorageToDefault(storageId));
    }

    @PutMapping(CHANGEUNITTODEFAULT)
    public ResponseEntity<Boolean> changeUnitToDefault(@RequestParam Long unitId) {
        return ResponseEntity.ok(service.changeUnitToDefault(unitId));
    }
}
