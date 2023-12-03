package com.apapedia.user.restservice;

import java.util.UUID;
import java.util.List;

import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.User;

public interface UserRestService {

    User getUserById(UUID id);

    User signUp(User user);

    List<User> getAllUser();

    User updateUser(UUID id, UpdateUserRequestDTO updateUserRequestDTO);
    
    void deleteUser(UUID id);
    
    void updateBalance(int amount, User user);
} 
