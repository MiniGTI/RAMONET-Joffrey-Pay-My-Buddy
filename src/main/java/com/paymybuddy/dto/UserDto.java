package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object to get the data input from the register form.
 * Used to parse data to register and create a new User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String emailCheck;
    private String password;
    private String passwordCheck;
    private String firstname;
    private String lastname;
}
