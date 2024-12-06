package com.zxc.naruto.security.controller;

import com.zxc.naruto.security.dto.UserChangeAdminDto;
import com.zxc.naruto.security.dto.UserChangeDto;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public List<User> findAll(@RequestParam(required = false,defaultValue = "0") Integer pageId, @RequestParam(required = false,defaultValue = "20") Integer pageSize) {
        return adminService.findAll(pageId, pageSize);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(adminService.findById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return adminService.delete(id);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> changeUser(@PathVariable Long id, @RequestBody UserChangeAdminDto user) {
        return adminService.changeUser(user,id);
    }
}
