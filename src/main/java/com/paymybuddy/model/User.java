package com.paymybuddy.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User model.
 * Used to stock all data of the user. Is create at the registration.
 * email is used to identify the user on the login page.
 * password is stocked encrypted by BCryptPasswordEncode.
 * role is always USER.
 * ----------
 * They're an OneToOne relation with the bank_account table.
 * At the creation of a User, a bankAccount is created too.
 * If the User delete his account, the bankAccount is deleted too.
 * ----------
 * They're a OneToMany relation with himself, but for a use the link table, is called ManyToMany.
 * The table user_buddys manage the relation with two columns, user_id and buddy_id.
 * The two columns refer to id of the user table.
 * user_id is the id of the Principal user.
 * buddy_id is the id of the user register like buddy.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 40)
    private String email;
    @Column(length = 61)
    private String password;
    @Column(length = 50)
    private String firstname;
    @Column(length = 50)
    private String lastname;
    @Column(length = 5)
    private String role;
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    
    @ManyToMany(
            cascade = CascadeType.MERGE,
               fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_buddys",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "buddys_id"))
    private List<User> buddys = new ArrayList<>();
    
    @ManyToMany(
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_buddys",
            joinColumns = @JoinColumn(name = "buddys_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> buddysOf = new ArrayList<>();
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
    
    public void addBuddy(User user) {
        buddys.add(user);
        user.getBuddys()
                .add(this);
    }
    
    public void removeBuddy(User user) {
        buddys.remove(user);
        user.getBuddys()
                .remove(this);
    }

}
