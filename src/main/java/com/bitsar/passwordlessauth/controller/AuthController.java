package com.bitsar.passwordlessauth.controller;

import com.bitsar.passwordlessauth.dto.ResponseWrap;
import com.bitsar.passwordlessauth.dto.request.ConfirmRegisterRequestDto;
import com.bitsar.passwordlessauth.dto.request.RegisterRequestDto;
import com.bitsar.passwordlessauth.dto.response.*;
import com.bitsar.passwordlessauth.service.AuthService;
import com.bitsar.passwordlessauth.util.JwtUtil;
import com.bitsar.passwordlessauth.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    public final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseWrap<RegisterResponseDto>> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseBuilder.getSuccessfulResponse(authService.signUp(registerRequestDto));
    }

    @PostMapping("/confirm-register")
    public ResponseEntity<ResponseWrap<ConfirmRegisterResponseDto>> confirmRegister(@RequestBody ConfirmRegisterRequestDto confirmRegisterRequestDto) {
        return ResponseBuilder.getSuccessfulResponse(authService.confirmSignUp(confirmRegisterRequestDto));
    }

    @GetMapping("/login")
    public ResponseEntity<ResponseWrap<LoginResponseDto>> login(@RequestParam String phoneNumber) {
        return ResponseBuilder.getSuccessfulResponse(authService.signIn(phoneNumber));
    }

    @GetMapping("/confirm-login")
    public ResponseEntity<ResponseWrap<ConfirmLoginResponseDto>> confirmLogin(@RequestParam String sessionId, @RequestParam String otp) {
        return ResponseBuilder.getSuccessfulResponse(authService.confirmSignIn(sessionId, otp));
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<ResponseWrap<ConfirmLoginResponseDto>> refreshToken(@RequestHeader("Authorization") String accessToken) {
        String cognitoUserName = JwtUtil.getUsernameFromToken(accessToken);
        return ResponseBuilder.getSuccessfulResponse(authService.refreshToken(cognitoUserName));
    }
    @GetMapping("/logout")
    public ResponseEntity<ResponseWrap<ConfirmLogoutResponseDto>> logout(@RequestHeader("Authorization") String accessToken) {
        return ResponseBuilder.getSuccessfulResponse(authService.logout(accessToken.replace("Bearer", "").trim(), JwtUtil.getUsernameFromToken(accessToken)));
    }
}
