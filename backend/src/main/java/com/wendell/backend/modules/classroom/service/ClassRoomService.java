package com.wendell.backend.modules.classroom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wendell.backend.common.security.AuthenticatedUserValidator;
import com.wendell.backend.modules.classroom.dto.UserDisciplineClassRoomListResponseDto;
import com.wendell.backend.modules.classroom.dto.UserClassRoomListResponseDto;
import com.wendell.backend.modules.classroom.repository.UserDisciplineClassRoomRepository;
import com.wendell.backend.modules.classroom.repository.UserClassRoomRepository;

@Service
public class ClassRoomService {

    @Autowired
    private UserClassRoomRepository userclassRoomRepository;

    @Autowired
    private UserDisciplineClassRoomRepository userDisciplineClassRoomRepository;

    @Autowired
    private AuthenticatedUserValidator authenticatedUserValidator;

    public List<UserClassRoomListResponseDto> listAvailableClassroomsByUserId(Long userId) {
        authenticatedUserValidator.validateUserId(userId);
        return userclassRoomRepository.findActiveClassroomsByUserId(userId);
    }

    public List<UserDisciplineClassRoomListResponseDto> listAvailableDisciplineByClassIDAndByUserId(Long classroomId,
            Long userId) {
        authenticatedUserValidator.validateUserId(userId);
        authenticatedUserValidator.validateClassroomId(classroomId);
        return userDisciplineClassRoomRepository.findDisciplinesByClassroomIdAndUserId(classroomId, userId);
    }
}
