package com.wendell.backend.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   
}