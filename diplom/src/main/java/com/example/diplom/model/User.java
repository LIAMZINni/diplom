package com.example.diplom.model;

import com.example.diplom.security.Role;
import jakarta.persistence.*;

import org.antlr.v4.runtime.misc.NotNull;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;
    @Column(unique = true, nullable = false)
    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


}
