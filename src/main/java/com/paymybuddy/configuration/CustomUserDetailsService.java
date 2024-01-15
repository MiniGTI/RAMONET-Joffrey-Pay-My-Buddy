package com.paymybuddy.configuration;


import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class to manage the UserDetailsService.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Call the slf4j class.
     */
    private final static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    /**
     * Call the UserRepository to perform the findByEmail to the User table.
     */
    private final UserRepository userRepository;
    
    /**
     * The class Constructor.
     *
     * @param userRepository to perform the findByEmail to the User table.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Method called by loadUserByUsername SpringSecurityConfig method
     *
     * @param email of the user who wishes to authenticate.
     * @return a UserDetails to become the Principal.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<User> optUser = userRepository.findByEmail(email);
        
        User user = null;
        UserDetails log = null;
        
        if(optUser.isEmpty()) {
            logger.error("User with email" + email + " not found.");
            throw new UsernameNotFoundException("User not found.");
        } else {
            user = optUser.get();
            
            log = new org.springframework.security.core.userdetails.User(email, user.getPassword(),
                    getGrantedAuthorities(user.getRole()));
        }
        
        return log;
    }
    
    /**
     * List of role authorisation.
     *
     * @param role String User for all User.
     * @return a list of role.
     */
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
