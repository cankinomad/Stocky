package org.berka.service;

import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.ErrorType;
import org.berka.exception.ProductManagerException;
import org.berka.manager.ICategoryManager;
import org.berka.manager.IStorageManager;
import org.berka.manager.IUnitManager;
import org.berka.mapper.IProductMapper;
import org.berka.repository.IProductRepository;
import org.berka.repository.entity.Product;
import org.berka.utility.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService extends ServiceManager<Product,Long> {


    private final IProductRepository repository;

    private final ICategoryManager categoryManager;

    private final IStorageManager storageManager;

    private final IUnitManager unitManager;

    public ProductService(JpaRepository<Product, Long> repository, IProductRepository repository1, ICategoryManager categoryManager, IStorageManager storageManager, IUnitManager unitManager) {
        super(repository);
        this.repository = repository1;
        this.categoryManager = categoryManager;
        this.storageManager = storageManager;
        this.unitManager = unitManager;
    }

    public Product createProduct(CreateRequestDto dto) {

        boolean existsByName = repository.existsByName(dto.getName());
        if (existsByName) {
            throw new ProductManagerException(ErrorType.PRODUCT_NAME_EXIST);
        }

        Boolean isCategoryExist = categoryManager.approveCategory(dto.getCategoryId()).getBody();
        if (!isCategoryExist) {
            throw new ProductManagerException(ErrorType.NO_CATEGORY_FOUND);
        }
        Boolean isStorageExist = storageManager.approveStorage(dto.getStorageId()).getBody();
        if (!isStorageExist) {
            throw new ProductManagerException(ErrorType.NO_STORAGE_FOUND);
        }

        Boolean isUnitExist = unitManager.approveUnit(dto.getUnitId()).getBody();
        if (!isUnitExist) {
            throw new ProductManagerException(ErrorType.NO_UNIT_FOUND);
        }

        return save(IProductMapper.INSTANCE.toProduct(dto));

    }

    public MessageResponseDto updateProduct(UpdateRequestDto dto) {
        Product product = repository.findById(dto.getId()).orElseThrow(() -> new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND));

        if (!product.getName().equals(dto.getName()) && repository.existsByName(dto.getName())) {
            throw new ProductManagerException(ErrorType.PRODUCT_NAME_EXIST);
        }

        if (product.getCategoryId() != dto.getCategoryId()) {
            Boolean isCategoryExist = categoryManager.approveCategory(dto.getCategoryId()).getBody();
            if (isCategoryExist) {
                product.setCategoryId(dto.getCategoryId());
            }else{
                throw new ProductManagerException(ErrorType.NO_CATEGORY_FOUND);
            }
        }

        if (product.getStorageId() != dto.getStorageId()) {
            Boolean isStorageExist = storageManager.approveStorage(dto.getStorageId()).getBody();
            if (isStorageExist) {
                product.setStorageId(dto.getStorageId());
            }else{
                throw new ProductManagerException(ErrorType.NO_STORAGE_FOUND);
            }
        }
        if (product.getUnitId() != dto.getUnitId()) {
            Boolean isUnitExist = unitManager.approveUnit(dto.getUnitId()).getBody();
            if (isUnitExist) {
                product.setUnitId(dto.getUnitId());
            }else{
                throw new ProductManagerException(ErrorType.NO_UNIT_FOUND);
            }
        }
        product.setName(dto.getName());
        product.setUnitId(dto.getUnitId());
        product.setAmount(dto.getAmount());

        update(product);

        return new MessageResponseDto("Urun bilgileri guncellenmistir");
    }

    public MessageResponseDto deleteProduct(Long id) {
        boolean existsById = repository.existsById(id);
        if (!existsById) {
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }
        deleteById(id);
        return new MessageResponseDto("Urun basariyla silinmistir");
    }

    public List<Product> findByStorageId(Long storageId) {
        List<Product> byStorageId = repository.findByStorageId(storageId);
        if (byStorageId.isEmpty()) {
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }
        return byStorageId;
    }

    public List<Product> findByCategoryId(Long categoryId) {
        List<Product> byCategoryId = repository.findByCategoryId(categoryId);
        if (byCategoryId.isEmpty()) {
            throw new ProductManagerException(ErrorType.PRODUCT_NOT_FOUND);
        }
        return byCategoryId;
    }

    public Boolean changeCategoryToDefault(Long categoryId) {
        List<Product> byCategoryId = repository.findByCategoryId(categoryId);

        for (Product product : byCategoryId) {
            product.setCategoryId(1L);
            update(product);
        }

        return true;
    }

    public Boolean changeStorageToDefault(Long storageId) {
        List<Product> byStorageId = repository.findByStorageId(storageId);
        for (Product product : byStorageId) {
            product.setStorageId(1L);
            update(product);
        }
        return true;
    }

    public Boolean changeUnitToDefault(Long unitId) {
        List<Product> byUnitId = repository.findByUnitId(unitId);

        for (Product product : byUnitId) {
            product.setUnitId(1L);
            update(product);
        }
        return true;
    }
}
