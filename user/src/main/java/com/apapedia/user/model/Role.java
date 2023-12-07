package com.apapedia.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.apapedia.user.model.Permission.SELLER_CREATE;
import static com.apapedia.user.model.Permission.SELLER_DELETE;
import static com.apapedia.user.model.Permission.SELLER_READ;
import static com.apapedia.user.model.Permission.SELLER_UPDATE;
import static com.apapedia.user.model.Permission.CUSTOMER_CREATE;
import static com.apapedia.user.model.Permission.CUSTOMER_DELETE;
import static com.apapedia.user.model.Permission.CUSTOMER_READ;
import static com.apapedia.user.model.Permission.CUSTOMER_UPDATE;

@RequiredArgsConstructor
public enum Role {
  SELLER(
          Set.of(
                  SELLER_READ,
                  SELLER_UPDATE,
                  SELLER_DELETE,
                  SELLER_CREATE,
                  CUSTOMER_READ,
                  CUSTOMER_UPDATE,
                  CUSTOMER_DELETE,
                  CUSTOMER_CREATE
          )
  ),
  CUSTOMER(
          Set.of(
                  CUSTOMER_READ,
                  CUSTOMER_UPDATE,
                  CUSTOMER_DELETE,
                  CUSTOMER_CREATE
          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
