package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BuddyRelation model
 *
 * Used to create the relation in users.
 */
@Entity
@Table(name = "user_buddy")
@Data
@NoArgsConstructor
public class BuddyRelation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "buddy_id")
    private Integer buddy_id;
    
    public BuddyRelation(Integer userId, Integer buddy_id) {
        this.userId = userId;
        this.buddy_id = buddy_id;
    }
}
