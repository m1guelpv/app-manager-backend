package dev.m1guel.appmanager.service;

import dev.m1guel.appmanager.dto.LoginDto;
import dev.m1guel.appmanager.dto.RegisterDto;
import dev.m1guel.appmanager.dto.ResponseDto;
import dev.m1guel.appmanager.exception.EmailAlreadyInUseException;
import dev.m1guel.appmanager.exception.InvalidPasswordException;
import dev.m1guel.appmanager.exception.EmailNotFoundException;
import dev.m1guel.appmanager.model.Role;
import dev.m1guel.appmanager.model.User;
import dev.m1guel.appmanager.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseDto<String>> login(LoginDto loginDto, HttpServletResponse response) {
        if (!userRepository.existsByEmail(loginDto.getEmail())) {
            throw new EmailNotFoundException(loginDto.getEmail());
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            String token = tokenService.generateToken((User) authentication.getPrincipal());
            response.setHeader("Authorization", "Bearer " + token);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseDto<>(true, "User successfully logged in", token)
            );
        } catch (BadCredentialsException e) {
            throw new InvalidPasswordException(loginDto.getEmail());
        }
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
        newUser.setRole(Role.USER);
        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDto<>(true, "User successfully registered", null)
        );
    }

    public ResponseEntity<ResponseDto<String>> logout(HttpServletResponse response) {
        response.setHeader("Authorization", null);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDto<>(true, "User successfully logged out", null)
        );
    }

}