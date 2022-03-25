package com.semihbkgr.springboottotp.user;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_details")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstname;

    private String lastname;

    private String email;

    private Boolean is2faEnabled;

    private String secretKey;

}
