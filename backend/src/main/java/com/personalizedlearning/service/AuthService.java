package com.personalizedlearning.service;

import com.personalizedlearning.dto.LoginRequest;
import com.personalizedlearning.dto.LoginResponse;
import com.personalizedlearning.dto.RegisterRequest;
import com.personalizedlearning.dto.UserDto;
import com.personalizedlearning.entity.StudentProfile;
import com.personalizedlearning.entity.User;
import com.personalizedlearning.exception.BadRequestException;
import com.personalizedlearning.exception.ResourceNotFoundException;
import com.personalizedlearning.repository.StudentProfileRepository;
import com.personalizedlearning.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered: " + request.getEmail());
        }

        String role = request.getRole() != null ? request.getRole().toUpperCase() : "STUDENT";
        if (!role.equals("STUDENT") && !role.equals("TEACHER") && !role.equals("ADMIN")) {
            role = "STUDENT";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setPhone(request.getPhone());
        user.setActive(true);

        User savedUser = userRepository.save(user);

        if ("STUDENT".equals(role)) {
            StudentProfile profile = new StudentProfile();
            profile.setUser(savedUser);
            profile.setCollegeName(request.getCollegeName() != null ? request.getCollegeName() : "University Institute");
            profile.setCourse(request.getCourse() != null ? request.getCourse() : "Computer Science & Engineering");
            profile.setYearOfStudy(request.getYearOfStudy() != null ? request.getYearOfStudy() : 3);
            profile.setProfileImage("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");
            studentProfileRepository.save(profile);
            savedUser.setStudentProfile(profile);
        }

        return toUserDto(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!user.getActive()) {
            throw new BadRequestException("Account is deactivated. Please contact admin.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = "AUTH_" + UUID.randomUUID().toString();

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Login successful"
        );
    }

    public UserDto getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return toUserDto(user);
    }

    public UserDto toUserDto(User user) {
        StudentProfile profile = user.getStudentProfile();
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone(),
                user.getActive(),
                user.getCreatedAt(),
                profile != null ? profile.getCollegeName() : null,
                profile != null ? profile.getCourse() : null,
                profile != null ? profile.getYearOfStudy() : null,
                profile != null ? profile.getProfileImage() : null
        );
    }
}
