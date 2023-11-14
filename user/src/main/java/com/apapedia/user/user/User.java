package com.apapedia.user.user;

import com.apapedia.user.token.Token;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  private final UUID id = UUID.randomUUID();

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false, unique = true)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "address", nullable = false)
  private String address;

  @Enumerated(EnumType.STRING)
  private Role role;

  @CreationTimestamp // Atribut ini akan diisi dengan waktu saat pertama kali entitas dibuat.
  @Column(name = "createdAt", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @UpdateTimestamp // Atribut ini akan diisi dengan waktu saat entitas terakhir diperbarui.
  @Column(name = "updatedAt")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
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