package com.personalizedlearning.service;

import com.personalizedlearning.dto.UserDto;
import com.personalizedlearning.entity.StudentProfile;
import com.personalizedlearning.entity.User;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.StudentProfileRepository;
import com.personalizedlearning.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository,
                       AuthService authService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.authService = authService;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(authService::toUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersByRole(String role) {
        return userRepository.findByRole(role.toUpperCase()).stream()
                .map(authService::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return authService.toUserDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getActive() != null) user.setActive(dto.getActive());

        if ("STUDENT".equalsIgnoreCase(user.getRole())) {
            StudentProfile profile = studentProfileRepository.findByUser(user)
                    .orElseGet(() -> {
                        StudentProfile p = new StudentProfile();
                        p.setUser(user);
                        return p;
                    });
            if (dto.getCollegeName() != null) profile.setCollegeName(dto.getCollegeName());
            if (dto.getCourse() != null) profile.setCourse(dto.getCourse());
            if (dto.getYearOfStudy() != null) profile.setYearOfStudy(dto.getYearOfStudy());
            if (dto.getProfileImage() != null) profile.setProfileImage(dto.getProfileImage());
            studentProfileRepository.save(profile);
            user.setStudentProfile(profile);
        }

        return authService.toUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto toggleUserActive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setActive(!user.getActive());
        return authService.toUserDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
