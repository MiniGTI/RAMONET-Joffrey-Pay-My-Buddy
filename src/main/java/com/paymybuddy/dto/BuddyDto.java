package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object to get email from the transfer form.
 * Used to parse the email of a User to add a Buddy.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuddyDto {
    
    private String email;
}
