package com.forma.Authorization_server.Repository;

import com.forma.Authorization_server.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<Object> findByEmail(String email);
}
