package com.wendell.backend.modules.classroom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.UserClassroom;
import com.wendell.backend.modules.classroom.dto.UserClassRoomListResponseDto;

@Repository
public interface UserClassRoomRepository extends JpaRepository<UserClassroom, Long> {

    @Query("""
                SELECT new com.wendell.backend.modules.classroom.dto.ClassroomListDto(
                    c.id,
                    c.name
                )
                FROM UserClassroom uc
                LEFT JOIN uc.classroom c
                WHERE uc.user.id = :userId
                AND uc.active = true
            """)
    List<UserClassRoomListResponseDto> findActiveClassroomsByUserId(Long userId);
    
}