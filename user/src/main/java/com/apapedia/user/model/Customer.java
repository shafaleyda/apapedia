package com.apapedia.user.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
// @PrimaryKeyJoinColumn(name = "users")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "customer")
public class Customer extends User {
    @NotNull
    @Column(name = "cart_id", nullable = false)
    private UUID cartId; // Merujuk pada tabel Cart


}
