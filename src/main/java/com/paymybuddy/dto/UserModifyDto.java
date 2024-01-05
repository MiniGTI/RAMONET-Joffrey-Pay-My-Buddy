package com.paymybuddy.dto;

import lombok.Data;
/**
 * Object instanced by the profile form to update a User's firstname, lastname, email field in the user table in the Database.
 */
@Data
public class UserModifyDto {

private String firstName;
private String lastname;
private String email;

}
