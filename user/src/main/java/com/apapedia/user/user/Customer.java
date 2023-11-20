package com.apapedia.user.user;

<<<<<<< HEAD
import java.util.UUID;

=======
>>>>>>> 3df004dc477b2fcdf40967613090d373b77d4980
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
=======
import java.util.UUID;

>>>>>>> 3df004dc477b2fcdf40967613090d373b77d4980
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

