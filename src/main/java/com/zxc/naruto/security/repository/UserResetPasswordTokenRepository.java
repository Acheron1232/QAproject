package com.zxc.naruto.security.repository;

import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.entity.UserResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserResetPasswordTokenRepository extends JpaRepository<UserResetPasswordToken,Long> {


    Optional<UserResetPasswordToken> findByToken(String token);
}
