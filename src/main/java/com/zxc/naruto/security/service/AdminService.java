package com.zxc.naruto.security.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxc.naruto.security.dto.UserChangeAdminDto;
import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.exception.custom.UserNotFoundException;
import com.zxc.naruto.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final UserRepository userRepository;

    public List<User> findAll(Integer pageId, Integer pageSize) {
        return userRepository.findAll(
                PageRequest.of(pageId, pageSize)
        ).toList();
    }

    public Optional<User> findById(Long id) {
        return userService.findById(id);
    }

    public ResponseEntity<String> changeUser(UserChangeAdminDto userDto, Long id){
        try{
            User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
            User newUser = userService.save(new User(
                    user.getId(),
                    userDto.getFirstName()!=null? userDto.getFirstName() : user.getFirstName(),
                    userDto.getLastName()!=null? userDto.getLastName() : user.getLastName(),
                    userDto.getEmail()!=null? userDto.getEmail() : user.getEmail(),
                    userDto.getPassword()!=null? userDto.getPassword() : user.getPassword(),
                    userDto.getPhoneNumber()!=null? userDto.getPhoneNumber() : user.getPhoneNumber(),
                    userDto.getRole()!=null? userDto.getRole() : user.getRole()
            ),false);
            System.out.println(newUser);
            System.out.println(userDto);
            System.out.println(user);
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(newUser));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> delete(Long id){
        return userService.delete(id) ? ResponseEntity.ok("Success") : ResponseEntity.badRequest().body("Error deleting");
    }
}
