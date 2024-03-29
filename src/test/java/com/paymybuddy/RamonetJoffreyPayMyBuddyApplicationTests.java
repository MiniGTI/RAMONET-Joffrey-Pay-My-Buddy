package com.paymybuddy;

import com.paymybuddy.controller.LoginController;
import com.paymybuddy.controller.RegisterController;
import com.paymybuddy.controller.RegisterSuccessController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class RamonetJoffreyPayMyBuddyApplicationTests {
    
    @Autowired
    private LoginController loginController;
    @Autowired
    private RegisterController registerController;
    @Autowired
    private RegisterSuccessController registerSuccessController;
    
    @Test
    void contextLoads() {
        assertThat(loginController).isNotNull();
        assertThat(registerController).isNotNull();
        assertThat(registerSuccessController).isNotNull();
    }
}
