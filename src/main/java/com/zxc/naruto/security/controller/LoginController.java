package com.zxc.naruto.security.controller;

//import com.zxc.naruto.mail.service.EmailService;
import com.zxc.naruto.security.dto.LoginRequest;
import com.zxc.naruto.security.dto.RegistrationRequest;
import com.zxc.naruto.security.exception.custom.EmailAlreadyExists;
import com.zxc.naruto.security.exception.custom.PhoneNumberAlreadyExists;
import com.zxc.naruto.security.service.LoginService;
import com.zxc.naruto.security.service.ResetPasswordService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class LoginController {
    private final LoginService loginService;
//    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody(required = false) RegistrationRequest registrationRequest, HttpServletResponse response) {
        try {
            response.addCookie(loginService.registration(registrationRequest));
        } catch (EmailAlreadyExists | PhoneNumberAlreadyExists e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("success");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            response.addCookie(loginService.login(request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body("Bad request");
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Bad credentials");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = loginService.logout(request);
        if (cookie != null) {
            response.addCookie(cookie);
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.badRequest().body("user did not log in");
    }

//    @GetMapping("/resetPassword")
//    public ResponseEntity<String> resetPassword(@RequestParam String email) {
//        String verifierEmail = loginService.findByEmail(email);
//        emailService.verifyEmail(email, RandomStringUtils.random(30));
//        return ResponseEntity.ok("success");
//    }
}
