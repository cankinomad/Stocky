package org.berka.repository;

import org.berka.repository.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStorageRepository extends JpaRepository<Storage,Long> {

    boolean existsByName(String name);

    Optional<Storage> findById(Long id);
}
