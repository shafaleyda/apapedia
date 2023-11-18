package com.apapedia.user.user;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer extends User{
    @NotNull
    @Column(name = "cart_id")
    private UUID cart_id;
}

