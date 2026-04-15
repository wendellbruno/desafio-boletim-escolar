package com.wendell.backend.modules.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wendell.backend.common.exception.BadRequestException;
import com.wendell.backend.common.exception.UnauthorizedException;
import com.wendell.backend.domain.User;
import com.wendell.backend.modules.user.dto.LoginRequestDto;
import com.wendell.backend.modules.user.dto.LoginResponseDto;
import com.wendell.backend.modules.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UnauthorizedException("Usuario ou senha invalidos"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Usuario ou senha invalidos");
        }

        List<Long> classrooms = user.getUserClassrooms().stream()
                .map(uc -> uc.getClassroom().getId())
                .toList();

        User userWithDisciplines = userRepository.findByIdWithDisciplines(user.getId())
                .orElseThrow(() -> new BadRequestException("Erro ao processar autenticacao"));

        List<Long> disciplines = userWithDisciplines.getUserDisciplineClassRooms().stream()
                .map(udc -> udc.getDiscipline().getId())
                .toList();

        String token = jwtService.generateToken(user.getUsername(), user.getId(), classrooms, disciplines);

        return new LoginResponseDto(user.getId(), user.getUsername(), token);
    }
}
