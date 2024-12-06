package com.zxc.naruto.security.service;

import com.zxc.naruto.security.dto.UserChangeDto;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.mapper.UserMapper;
import com.zxc.naruto.security.repository.UserRepository;
import com.zxc.naruto.security.exception.custom.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testChangeUser_UserNotFound() {
        Authentication authentication = mock(Authentication.class);
        Principal principal = mock(Principal.class);
        UserChangeDto changeDto = new UserChangeDto();
        changeDto.setEmail("new.email@example.com");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        var response = userService.changeUser(principal, changeDto, null);

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Unauthorized"));
    }
}
