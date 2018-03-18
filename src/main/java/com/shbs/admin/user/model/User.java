package com.shbs.admin.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    @Id
    private String username;
    private String password;
    private Short enabled;
}
