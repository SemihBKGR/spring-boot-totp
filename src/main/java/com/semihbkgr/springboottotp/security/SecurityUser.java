package com.semihbkgr.springboottotp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class SecurityUser extends User {

    private static final Collection<GrantedAuthority> USER_GRANTED_AUTHORITY_COLLECTION = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    public SecurityUser(com.semihbkgr.springboottotp.user.User user) {
        super(user.getUsername(), user.getPassword(), USER_GRANTED_AUTHORITY_COLLECTION);
    }

}
