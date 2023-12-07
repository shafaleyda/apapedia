package com.apapedia.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "balance", nullable = false)
    private int balance;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    // List<GrantedAuthority> authorities = new ArrayList<>();
    // if (this instanceof Seller) {
    // authorities.add(new SimpleGrantedAuthority("SELLER"));
    // } else if (this instanceof Customer) {
    // authorities.add(new SimpleGrantedAuthority("CUSTOMER"));
    // }
    // return authorities;
    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}