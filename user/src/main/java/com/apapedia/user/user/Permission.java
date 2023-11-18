package com.apapedia.user.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    SELLER_READ("seller:read"),
    SELLER_UPDATE("seller:update"),
    SELLER_CREATE("seller:create"),
    SELLER_DELETE("seller:delete"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMER_CREATE("customer:create"),
    CUSTOMER_DELETE("customer:delete")

    ;

    @Getter
    private final String permission;
}