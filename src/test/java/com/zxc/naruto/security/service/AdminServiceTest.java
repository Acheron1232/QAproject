package com.zxc.naruto.security.service;

import com.zxc.naruto.security.dto.UserChangeAdminDto;
import com.zxc.naruto.security.entity.Role;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.exception.custom.UserNotFoundException;
import com.zxc.naruto.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeUser_Success() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("john.doe@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        UserChangeAdminDto userChangeAdminDto = new UserChangeAdminDto("John", "Doe", "john.doe@example.com", "newPassword", Role.ADMIN, "1234567890");

        when(userService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userService.save(any(User.class), eq(false))).thenReturn(mockUser);

        ResponseEntity<String> response = adminService.changeUser(userChangeAdminDto, 1L);

        assertEquals(200, response.getStatusCodeValue());
        System.out.println(response.getBody());
        assertTrue(response.getBody().contains("john.doe@example.com"));
        verify(userService).findById(1L);
        verify(userService).save(any(User.class), eq(false));
    }

    @Test
    void testChangeUser_UserNotFound() {
        UserChangeAdminDto userChangeAdminDto = new UserChangeAdminDto("John", "Doe", "john.doe@example.com", "newPassword", Role.ADMIN, "1234567890");

        when(userService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = adminService.changeUser(userChangeAdminDto, 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody()); // Ensure the body is not null
        assertTrue(response.getBody().contains("User not found"));
    }

    @Test
    void testDeleteUser_Success() {
        when(userService.delete(1L)).thenReturn(true);

        ResponseEntity<String> response = adminService.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Success"));
    }

    @Test
    void testDeleteUser_Failure() {
        when(userService.delete(1L)).thenReturn(false);

        ResponseEntity<String> response = adminService.delete(1L);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Error deleting"));
    }
}
