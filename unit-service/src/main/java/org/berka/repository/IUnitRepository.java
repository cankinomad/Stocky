package org.berka.repository;

import org.berka.repository.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUnitRepository extends JpaRepository<Unit,Long> {

    boolean existsByName(String name);

}
