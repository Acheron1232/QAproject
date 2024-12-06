package com.zxc.naruto.security.mapper;

import com.zxc.naruto.security.dto.UserChangeDto;
import com.zxc.naruto.security.dto.UserGetDto;
import com.zxc.naruto.security.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserGetDto mapToUserGetDto(User user){
        return new UserGetDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getRole());
    }

    public User mapFromUserChangeDto(UserChangeDto userChangeDto,User user){
        return new User(user.getId(),
                userChangeDto.getFirstName()!= null? userChangeDto.getFirstName() : user.getFirstName(),
                userChangeDto.getLastName()!= null? userChangeDto.getLastName() : user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                userChangeDto.getPhoneNumber()!= null? userChangeDto.getPhoneNumber() : user.getPhoneNumber(),
                user.getRole()
                );
    }
}
