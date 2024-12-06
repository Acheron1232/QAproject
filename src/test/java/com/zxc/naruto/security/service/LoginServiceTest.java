package com.zxc.naruto.security.service;

import com.zxc.naruto.security.dto.LoginRequest;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password123");
        User mockUser = new User();
        mockUser.setEmail("john.doe@example.com");
        
        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(mockUser)).thenReturn("fakeJwtToken");

        Cookie cookie = loginService.login(loginRequest);

        assertNotNull(cookie);
        assertEquals("accessToken", cookie.getName());
        assertEquals("fakeJwtToken", cookie.getValue());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void testLogin_BadCredentials() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "wrongPassword");

        // Mock the authentication manager to throw BadCredentialsException when invalid credentials are provided
        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }
}
