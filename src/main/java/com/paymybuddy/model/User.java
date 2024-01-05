package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * User model.
 */
@Entity
@Data
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String role;
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    
    @OneToMany(
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<User> buddys;
    

    public User(String email, String password, String firstname, String lastname, String role,
                BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }
    
    public User(Integer id, String email, String password, String firstname, String lastname, String role,
                BankAccount bankAccount) {
        this.id = id;
        this.bankAccount = bankAccount;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }
}
