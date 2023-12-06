package com.apapedia.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpMethod;
import static com.apapedia.user.model.SellerCategory.BIASA;

import com.apapedia.user.dto.UserMapper;
import com.apapedia.user.dto.request.RegisterRequest;
import com.apapedia.user.dto.response.AuthenticationResponse;

import jakarta.validation.Valid;

@Controller
public class UserController {

    //FRONTEND BLM SELESAI

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        var userDTO = new RegisterRequest();
        model.addAttribute("userDTO", userDTO);

        return "register.html";
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute RegisterRequest userDTO, BindingResult bindingResult,
            Model model) {

        System.out.println("TESTTTTTT");
        if (bindingResult.hasErrors()) {
            // Validasi gagal, kembalikan error
            var errorMessage = "Data yang anda kirimkan tidak valid";

            model.addAttribute("errorMessage", errorMessage);

            return "error-view";
        }

        var user = userMapper.createUserRequestDTOToUser(userDTO);

        System.out.println("TEST 1");
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8081/api/authentication/register";

        System.out.println("TEST 2");

        // Create a RegisterRequest object or prepare the request body
        RegisterRequest requestBody = new RegisterRequest();
        requestBody.setName(user.getName());
        requestBody.setUsername(user.getUsername());
        requestBody.setAddress(user.getAddress());
        requestBody.setPassword(user.getPassword());
        requestBody.setEmail(user.getEmail());
        requestBody.setRole(user.getRole());
        requestBody.setCategory(BIASA); // Initialize with actual data

        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(requestBody);

        System.out.println("TEST 3");

        // Make a POST request to the registration endpoint
        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                AuthenticationResponse.class);

        // Handle the response as needed
        if (response.getStatusCode() == HttpStatus.OK) {
            AuthenticationResponse authenticationResponse = response.getBody();

            // Jika registrasi berhasil, kamu dapat melakukan sesuatu, misalnya:
            if (authenticationResponse != null) {
                // Registrasi sukses, bisa menampilkan pesan atau melakukan tindakan lain
                System.out.println("Registrasi berhasil untuk pengguna: ");
            } else {
                // Respons OK tetapi respons body kosong atau tidak sesuai
                System.out.println("Registrasi berhasil dengan respons kosong atau tidak valid");
            }
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            // Respons Bad Request, misalnya validasi gagal
            System.out.println("Registrasi gagal karena permintaan tidak valid atau data tidak lengkap");
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            // Respons Internal Server Error, ada masalah di server
            System.out.println("Registrasi gagal karena masalah internal server");
        } else {
            // Respons lainnya
            System.out.println("Registrasi gagal dengan kode status: " + response.getStatusCode());
        }

        return "login.html";

    }

}
