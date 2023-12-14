package com.apapedia.frontend.dto.request;

//-----------------TODO-----------------//

import java.util.UUID;

import com.apapedia.frontend.dto.RegisterSellerRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO extends RegisterSellerRequestDTO {
    private String id;
}