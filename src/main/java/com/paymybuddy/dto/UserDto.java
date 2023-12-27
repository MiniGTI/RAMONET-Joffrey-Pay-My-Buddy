package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object instanced by the register form to create the User object for saving in the Database.
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
    private String role;
}
