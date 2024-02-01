package org.berka.repository;

import org.berka.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAuthRepository extends JpaRepository<Auth, Long> {


    boolean existsByUsername(String username);

    Optional<Auth> findByUsernameAndPassword(String username, String password);

    Optional<Auth> findById(Long id);

}
