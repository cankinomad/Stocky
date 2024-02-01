package org.berka.repository;

import org.berka.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    Optional<User> findByAuthId(Long authId);

    boolean existsByAuthId(Long authId);
}
