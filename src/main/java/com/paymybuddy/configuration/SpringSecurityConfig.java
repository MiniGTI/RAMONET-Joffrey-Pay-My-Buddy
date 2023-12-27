package com.paymybuddy.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Class to secure the authentication with SpringBoot Security.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    
    /**
     * Call to the CustomUserDetailsService class.
     */
    private final CustomUserDetailsService customUserDetailsService;
    
    /**
     * SpringSecurityConfig Constructor.
     *
     * @param customUserDetailsService CustomUserDetailService.
     */
    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    
    /**
     * Method to configure the authentication and restrictions.
     *
     * @param http HttpSecurity.class call, allows to apply the security filter string to HTTP requests.
     * @return a filter string configuration.
     * @throws Exception generated by authorizeHttpRequests.
     *                   Only login and register pages are allowed for all.
     *                   Juste the USER role exist.
     *                   RememberMe generate a remember-me cookie called remember-me-cookie.
     *                   Logout invalid the session and delete the JSESSIONID cookie.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/*")
                            .permitAll();
                    auth.requestMatchers("/register", "/registerSuccess")
                            .permitAll();
                    auth.requestMatchers("/authenticated/*")
                            .hasRole("USER");
                    auth.anyRequest()
                            .authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/login");
                    form.usernameParameter("email");
                    form.passwordParameter("password");
                    form.successForwardUrl("/");
                    form.failureUrl("/login?error");
                    form.permitAll();
                })
                .rememberMe(remember -> {
                    remember.rememberMeParameter("remember-me");
                    remember.key("its-me-key");
                    remember.rememberMeCookieName("remember-me-cookie");
                    remember.useSecureCookie(true);
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout");
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                    logout.permitAll();
                })
                .build();
    }
    
    /**
     * Class to encode password.
     *
     * @return An encoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Methode to valid or not the authentication of the customDetailsService.
     *
     * @param http                  HttpSecurity.class call, allows to apply the security filter string to HTTP requests.
     * @param bCryptPasswordEncoder The password encoder.
     * @return authenticationManagerBuilder, give the authentication
     * @throws Exception generated by userDetailsService and build.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
}
