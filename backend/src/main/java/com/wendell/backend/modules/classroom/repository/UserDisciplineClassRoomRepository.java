package com.wendell.backend.modules.classroom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wendell.backend.domain.UserDisciplineClassRoom;
import com.wendell.backend.modules.classroom.dto.UserDisciplineClassRoomListResponseDto;

@Repository
public interface UserDisciplineClassRoomRepository extends JpaRepository<UserDisciplineClassRoom, Long> {

    @Query("""
                SELECT DISTINCT new com.wendell.backend.modules.classroom.dto.UserDisciplineClassRoomListResponseDto(
                    d.id,
                    d.name
                )
                FROM UserDisciplineClassRoom sc
                JOIN sc.discipline d
                WHERE sc.classroom.id = :classroomId
                and sc.user.id = :userId
                AND sc.active = true
            """)
    List<UserDisciplineClassRoomListResponseDto> findDisciplinesByClassroomIdAndUserId(Long classroomId, Long userId);
}
