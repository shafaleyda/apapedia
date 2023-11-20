package com.apapedia.user.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.apapedia.user.user.Permission.SELLER_CREATE;
import static com.apapedia.user.user.Permission.SELLER_DELETE;
import static com.apapedia.user.user.Permission.SELLER_READ;
import static com.apapedia.user.user.Permission.SELLER_UPDATE;
import static com.apapedia.user.user.Permission.CUSTOMER_CREATE;
import static com.apapedia.user.user.Permission.CUSTOMER_DELETE;
import static com.apapedia.user.user.Permission.CUSTOMER_READ;
import static com.apapedia.user.user.Permission.CUSTOMER_UPDATE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

<<<<<<< HEAD
<<<<<<< HEAD
  USER(Collections.emptySet()),
  SELLER(
          Set.of(
                  SELLER_READ,
                  SELLER_UPDATE,
                  SELLER_DELETE,
                  SELLER_CREATE
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
=======
=======
>>>>>>> 2160e5ce41fdfc53c6121668711502aa8061d87d
    USER(Collections.emptySet()),
    SELLER(
            Set.of(
                    SELLER_READ,
                    SELLER_UPDATE,
                    SELLER_DELETE,
                    SELLER_CREATE
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
<<<<<<< HEAD
>>>>>>> 3df004dc477b2fcdf40967613090d373b77d4980
=======
>>>>>>> 2160e5ce41fdfc53c6121668711502aa8061d87d

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