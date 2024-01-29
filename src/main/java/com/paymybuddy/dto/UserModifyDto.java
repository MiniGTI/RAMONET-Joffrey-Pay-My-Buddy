package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object to get the data input from the profileModify form.
 * Used to parse data to update the Principal User's firstname and/or lastname attribute.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyDto {

private String firstName;
private String lastname;

}
