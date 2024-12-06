package com.zxc.naruto.security.dto;

import com.zxc.naruto.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
}
