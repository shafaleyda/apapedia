package com.apapedia.user.repository;

import com.apapedia.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDb extends JpaRepository<User, UUID> {
    List<User> findAll();
    Optional<User> findById(UUID id);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    User findByUsername(String username);
}