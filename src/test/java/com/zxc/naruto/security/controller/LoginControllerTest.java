package com.zxc.naruto.security.controller;

import com.zxc.naruto.security.dto.LoginRequest;
import com.zxc.naruto.security.dto.RegistrationRequest;
import com.zxc.naruto.security.exception.custom.EmailAlreadyExists;
import com.zxc.naruto.security.exception.custom.PhoneNumberAlreadyExists;
import com.zxc.naruto.security.service.LoginService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void testRegister_Success() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        Cookie cookie = new Cookie("token", "dummyToken");
        when(loginService.registration(any(RegistrationRequest.class))).thenReturn(cookie);

        mockMvc.perform(post("/api/v2/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(loginService).registration(any(RegistrationRequest.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() throws Exception {
        when(loginService.registration(any(RegistrationRequest.class))).thenThrow(new EmailAlreadyExists("Email already exists"));

        mockMvc.perform(post("/api/v2/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));

        verify(loginService).registration(any(RegistrationRequest.class));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        Cookie cookie = new Cookie("token", "dummyToken");
        when(loginService.login(any(LoginRequest.class))).thenReturn(cookie);

        mockMvc.perform(post("/api/v2/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(loginService).login(any(LoginRequest.class));
    }

    @Test
    void testLogin_BadCredentials() throws Exception {
        when(loginService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/v2/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad credentials"));

        verify(loginService).login(any(LoginRequest.class));
    }

    @Test
    void testLogout_Success() throws Exception {
        Cookie cookie = new Cookie("token", "");
        when(loginService.logout(any())).thenReturn(cookie);

        mockMvc.perform(post("/api/v2/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));

        verify(loginService).logout(any());
    }

    @Test
    void testLogout_UserNotLoggedIn() throws Exception {
        when(loginService.logout(any())).thenReturn(null);

        mockMvc.perform(post("/api/v2/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("user did not log in"));

        verify(loginService).logout(any());
    }
}