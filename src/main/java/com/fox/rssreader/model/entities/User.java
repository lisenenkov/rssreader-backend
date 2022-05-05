package com.fox.rssreader.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String login;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean showViewed = false;

    @Column(nullable = false)
    private Boolean showIgnored = false;

    @OneToMany(mappedBy ="user", fetch = FetchType.LAZY)
    private Set<UserAccessToken> accessTokens = new HashSet<>();
}
