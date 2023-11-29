package com.apapedia.user.dto;

import org.mapstruct.Mapper;

import com.apapedia.user.dto.request.RegisterRequest;
import com.apapedia.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User createUserRequestDTOToUser(RegisterRequest createUserRequestDTO);

    
}
