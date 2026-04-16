package com.wendell.backend.modules.grade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.Student;

@Repository
public interface StudentLookupRepository extends JpaRepository<Student, Long> {
}
