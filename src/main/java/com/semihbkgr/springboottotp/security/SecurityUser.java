package com.semihbkgr.springboottotp.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class SecurityUser extends User {

    private static final Collection<GrantedAuthority> USER_GRANTED_AUTHORITY_COLLECTION = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    private boolean is2faEnabled;
    private String secretKey;

    public SecurityUser(com.semihbkgr.springboottotp.user.User user) {
        super(user.getUsername(), user.getPassword(), USER_GRANTED_AUTHORITY_COLLECTION);
        this.is2faEnabled = user.getUserDetail().getIs2faEnabled();
        this.secretKey = user.getUserDetail().getSecretKey();
    }

}
