package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object to get the data input from the passwordModify form.
 * Used to parse data to update the Principal User's password attribute.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {

private String oldPassword;
private String newPassword;
private String newPasswordCheck;

}
