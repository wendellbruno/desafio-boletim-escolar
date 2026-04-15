package com.wendell.backend.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u  
            FROM User u
            LEFT JOIN FETCH u.userClassrooms uc
            WHERE u.username = :username
            """)
    Optional<User> findByUsername(@Param("username") String username);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            LEFT JOIN FETCH u.userDisciplineClassRooms udc
            WHERE u.id = :userId
            """)
    Optional<User> findByIdWithDisciplines(@Param("userId") Long userId);
}