package com.semihbkgr.springboottotp.security;

import com.semihbkgr.springboottotp.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserService userService;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userService.findByUsername(username);
            return new SecurityUser(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("user '%s' not found".formatted(username), e);
        }
    }

}
