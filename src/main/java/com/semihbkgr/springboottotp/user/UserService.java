package com.semihbkgr.springboottotp.user;

public interface UserService {

    User save(User user);

    User findByUsername(String username);

}
