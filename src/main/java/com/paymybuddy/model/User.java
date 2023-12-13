package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    @OneToMany(
    fetch = FetchType.EAGER
    )
    @JoinColumn(name = "user_id")
    private List<User> buddys;
    
    public User(String email, String password, String firstname, String lastname, BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
