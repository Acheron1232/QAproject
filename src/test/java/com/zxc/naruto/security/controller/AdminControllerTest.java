package com.zxc.naruto.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.naruto.security.dto.UserChangeAdminDto;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testFindAllUsers_Success() throws Exception {
        User mockUser = new User(); // Create a mock User object
        mockUser.setId(1L); // Set mock data
        when(adminService.findAll(0, 20)).thenReturn(Collections.singletonList(mockUser));

        mockMvc.perform(get("/api/v2/admin/users")
                .param("pageId", "0")
                .param("pageSize", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(adminService).findAll(0, 20);
    }

    @Test
    void testFindUserById_Success() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        when(adminService.findById(1L)).thenReturn(java.util.Optional.of(mockUser));

        mockMvc.perform(get("/api/v2/admin/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(adminService).findById(1L);
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        when(adminService.delete(1L)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/v2/admin/users/{id}", 1L))
                .andExpect(status().isOk());

        verify(adminService).delete(1L);
    }

    @Test
    void testChangeUser_Success() throws Exception {
        UserChangeAdminDto userChangeAdminDto = new UserChangeAdminDto(); // Initialize with necessary fields
        User mockUser = new User();
        mockUser.setId(1L);
        when(adminService.changeUser(any(UserChangeAdminDto.class), eq(1L))).thenReturn(ResponseEntity.ok(new ObjectMapper().writeValueAsString(mockUser)));

        mockMvc.perform(put("/api/v2/admin/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(adminService).changeUser(any(UserChangeAdminDto.class), eq(1L));
    }
}
