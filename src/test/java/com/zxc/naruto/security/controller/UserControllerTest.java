package com.zxc.naruto.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.naruto.security.dto.PasswordChangeDto;
import com.zxc.naruto.security.dto.UserChangeDto;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUser_Success() throws Exception {

        when(userService.getCurrentUser(any())).thenReturn(ResponseEntity.ok("User details"));

        mockMvc.perform(get("/api/v2/user/getUser"))
                .andExpect(status().isOk())
                .andExpect(content().string("User details"));

        verify(userService).getCurrentUser(any());
    }

    @Test
    void testChangeUser_Success() throws Exception {
        // Create a mock UserChangeDto and User object for the test
        UserChangeDto userChangeDto = new UserChangeDto();
        User mockUser = new User(); // You can set the necessary fields of mockUser

        // Mock the userService.changeUser method
        when(userService.changeUser(any(), any(), any())).thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(mockUser)));

        // Perform the test with the mockMvc
        mockMvc.perform(put("/api/v2/user/changeUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ /* expected JSON response representing mockUser */ }"));

        // Verify that the userService.changeUser method was called
        verify(userService).changeUser(any(), any(), any());
    }


    @Test
    void testChangePassword_Success() throws Exception {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        when(userService.changePassword(any(), any(), any())).thenReturn(ResponseEntity.ok("Password changed"));

        mockMvc.perform(patch("/api/v2/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed"));

        verify(userService).changePassword(any(), any(), any());
    }
}
