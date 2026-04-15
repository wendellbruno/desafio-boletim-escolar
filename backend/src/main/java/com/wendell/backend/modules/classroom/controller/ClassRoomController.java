package com.wendell.backend.modules.classroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wendell.backend.modules.classroom.dto.UserClassRoomListResponseDto;
import com.wendell.backend.modules.classroom.service.ClassRoomService;

@RestController
@RequestMapping("/classrooms")
public class ClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/user/{userId}")
    public List<UserClassRoomListResponseDto> listAvailableClassrooms(@PathVariable Long userId) {
        return classRoomService.listAvailableClassroomsByUserId(userId);
    }
}
