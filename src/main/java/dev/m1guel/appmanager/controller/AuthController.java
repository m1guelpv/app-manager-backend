package dev.m1guel.appmanager.controller;

import dev.m1guel.appmanager.dto.LoginDto;
import dev.m1guel.appmanager.dto.RegisterDto;
import dev.m1guel.appmanager.dto.ResponseDto;
import dev.m1guel.appmanager.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<String>> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        return authService.login(loginDto, response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> register(@RequestBody @Valid RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<String>> logout(HttpServletResponse response) {
        return authService.logout(response);
    }

}
