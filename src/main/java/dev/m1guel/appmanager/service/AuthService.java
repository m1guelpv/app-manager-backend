package dev.m1guel.appmanager.service;

import dev.m1guel.appmanager.dto.LoginDto;
import dev.m1guel.appmanager.dto.RegisterDto;
import dev.m1guel.appmanager.dto.ResponseDto;
import dev.m1guel.appmanager.exception.EmailAlreadyInUseException;
import dev.m1guel.appmanager.exception.InvalidPasswordException;
import dev.m1guel.appmanager.exception.UserNotFoundException;
import dev.m1guel.appmanager.model.User;
import dev.m1guel.appmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseDto<String>> login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(
                () -> new UserNotFoundException(loginDto.getEmail())
        );
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException(loginDto.getEmail());
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDto<>(true, "User successfully Logged in", null)
        );
    }

    public ResponseEntity<ResponseDto<String>> register(RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException(registerDto.getEmail());
        }
        User newUser = new User();
        newUser.setFirstName(registerDto.getFirstName());
        newUser.setLastName(registerDto.getLastName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDto<>(true, "User successfully registered", null)
        );
    }

}