package org.berka.repository;

import org.berka.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long> {

    boolean existsByName(String name);

    List<Product> findByStorageId(Long storageId);

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByUnitId(Long unitId);
}
