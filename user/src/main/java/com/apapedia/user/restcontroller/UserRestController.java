package com.apapedia.user.restcontroller;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.User;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.restservice.UserRestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserRestService userRestService;

    @GetMapping(value = "/{id}")
    private User getUserById(@PathVariable("id") UUID id) {
        User user = userRestService.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    @GetMapping(value = "/all")
    private List<User> getAllUser() {
        return userRestService.getAllUser();
    }

    @PutMapping(value = "/update/{id}")
    private User updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UpdateUserRequestDTO userDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field");
        } else {
            userDTO.setId(id);

            return userRestService.updateUser(id, userDTO);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    private void deleteUser(@PathVariable("id") UUID id) {

        userRestService.deleteUser(id);

    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<String> updateBalance(
            @PathVariable("id") UUID userId,
            @RequestParam(value = "amount", required = true) int amount) {
        try {
            // Mengambil user berdasarkan ID
            User user = userRestService.getUserById(userId);

            // Jika user tidak ditemukan, lemparkan ResponseStatusException dengan status
            // NOT_FOUND
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            userRestService.updateBalance(amount, user);

            return ResponseEntity.ok("Balance updated successfully. New balance: " + user.getBalance());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update balance: " + e.getMessage());
        }
    }

}
