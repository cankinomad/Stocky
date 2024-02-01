package org.berka.repository;

import org.berka.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {

    boolean existsById(Long id);

    Optional<Category> findById(Long id);

    boolean existsByName(String name);
}
