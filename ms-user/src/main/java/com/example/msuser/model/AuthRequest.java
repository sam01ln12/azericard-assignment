package com.example.msuser.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthRequest {

    @NotBlank(message = "Username cannot be empty or blank")
    private String username;
    @NotBlank(message = "Password cannot be empty or blank")
    private String password;


}
