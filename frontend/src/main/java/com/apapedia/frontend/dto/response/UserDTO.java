// package com.apapedia.frontend.dto.response;

// import lombok.*;

<<<<<<< HEAD
// import java.time.LocalDateTime;
// import java.util.List;
=======
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String password;
    private String email;
    private int balance;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;
    private String category;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;

    // Constructors, getters, and setters
}
>>>>>>> 567df3d833c8f5102f893324c6301e491d198294

// @Getter
// @Setter
// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class UserDTO {
//     private String id;
//     private String name;
//     private String username;
//     private String password;
//     private String email;
//     private int balance;
//     private String address;
//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt;
//     private String role;
//     private String category;
//     private boolean accountNonLocked;
//     private boolean accountNonExpired;
//     private boolean credentialsNonExpired;
//     private List<Authority> authorities;
//     private boolean enabled;

//     // Constructors, getters, and setters
// }