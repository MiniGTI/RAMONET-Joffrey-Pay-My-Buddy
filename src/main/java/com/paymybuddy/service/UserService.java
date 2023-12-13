package com.paymybuddy.service;

import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> getBy(Integer id) {
        return userRepository.findById(id);
    }
    
    public User save(User user) {
       return userRepository.save(user);
    }
    
    public void deleteBy(Integer id) {
        userRepository.deleteById(id);
    }
}
