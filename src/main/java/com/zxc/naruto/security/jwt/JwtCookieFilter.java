package com.zxc.naruto.security.jwt;



import com.zxc.naruto.security.entity.User;
import com.zxc.naruto.security.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = cookies != null ? Arrays.stream(cookies).filter(cookie1 -> cookie1.getName().equals("accessToken")).findFirst().orElse(null) : null;
        String jwt = null;
        String username = null;
        List<String> roles = null;
        if (cookie != null) {
            jwt = cookie.getValue();
            try {
                username = jwtUtil.getUsername(jwt);
                User user = userService.findByEmail(username).orElse(null);
                if (user == null) {
                    Cookie expiredCookie = new Cookie("accessToken", null);
                    expiredCookie.setMaxAge(0);
                    expiredCookie.setPath("/");
                    response.addCookie(expiredCookie);
                    response.sendError(403, "User does not exist");
                    return;
                }
                roles = jwtUtil.getRoles(jwt);
                if (user != null && !roles.getFirst().equals(user.getRole().getAuthority())) {
                    roles = List.of(user.getRole().getAuthority());
                    response.addCookie(userService.createCookie(jwtUtil.generateToken(user)));
                }

            } catch (ExpiredJwtException e) {
                response.sendError(403, "Token has expired");
                return;
            } catch (Exception e) {
                response.sendError(403, "Token is  invalid");
                return;
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
