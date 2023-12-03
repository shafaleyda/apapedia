package com.apapedia.user.restservice;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.apapedia.user.dto.request.UpdateUserRequestDTO;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.User;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserRestServiceImpl implements UserRestService {

    @Autowired
    private UserDb userDb;

    @Autowired
    private CustomerDb customerDb;

    @Autowired
    private SellerDb sellerDb;

    @Override
    public User getUserById(UUID id) {
        return userDb.findById(id).get();
    }

    @Override
    public User signUp(User user) {
        if (userDb.existsByUsername(user.getUsername()) || userDb.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Username or email already exists.");
        }
        return null;
    }

    @Override 
    public List<User> getAllUser() {
    
        return userDb.findAll();
    }

    @Override
    public User updateUser(UUID id, UpdateUserRequestDTO userDTO){
        User user = userDb.findById(id).orElse(null);
        if (user != null) {
            user.setAddress(userDTO.getAddress());
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setUsername(userDTO.getUsername());

            userDb.save(user);
            return user;
        }

        return null;
    }

    @Override
    public void deleteUser(UUID id) {
        // Lakukan pengecekan terlebih dahulu jika pengguna ditemukan atau tidak
        User user = userDb.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Hapus pengguna dari database
        userDb.delete(user);
    }

    @Override
    public void updateBalance(int amount, User user){
        // Mengambil balance saat ini
        int currentBalance = user.getBalance();

        // Update balance (Tambah atau Kurangi sesuai dengan permintaan)
        user.setBalance(currentBalance + amount);

        // Simpan perubahan pada user
        userDb.save(user);
    }
    
}
