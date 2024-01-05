package com.paymybuddy.dto;

import lombok.Data;

/**
 * Object instanced by the profile/password form to update a User's password field in the user table in the Database.
 */
@Data
public class PasswordDto {

private String oldPassword;
private String newPassword;
private String newPasswordCheck;

}
