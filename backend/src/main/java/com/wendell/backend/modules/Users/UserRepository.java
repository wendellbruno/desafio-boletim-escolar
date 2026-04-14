package com.wendell.backend.modules.Users;

import com.wendell.backend.infra.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // You can add custom methods here, ex.:
    // User findByUsername(String username);
}