package com.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object instanced by the addBuddy form to add the User object in the buddyList in the Database.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuddyDto {
    
    private String email;
}
