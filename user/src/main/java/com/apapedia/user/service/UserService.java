package com.apapedia.user.service;

import com.apapedia.user.dto.request.LoginJwtRequestDTO;

public interface UserService {

    String loginJwtAdmin(LoginJwtRequestDTO loginJwtRequestDTO);
}