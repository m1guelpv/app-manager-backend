package dev.m1guel.appmanager.controller;

import dev.m1guel.appmanager.dto.LoginDto;
import dev.m1guel.appmanager.dto.RegisterDto;
import dev.m1guel.appmanager.dto.ResponseDto;
import dev.m1guel.appmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<String>> login(@RequestBody @Valid LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> login(@RequestBody @Valid RegisterDto registerDto) {
        return authService.register(registerDto);
    }

}
