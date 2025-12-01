package com.gameflix.service;

import com.gameflix.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * Test 1:
     * It validates that a new user can be registered successfully. A successful registration should return a non-null User object with an encoded password.
     */
    @Test
    void registerUser_ShouldSaveAndReturnUser() {

        User newUser = new User();
        newUser.setUsername("testuser_main");
        newUser.setPassword("password123");

        User savedUser = userService.registerUser(newUser);

        Assertions.assertNotNull(savedUser, "The saved user should not be null.");
        Assertions.assertEquals("testuser_main", savedUser.getUsername(), "The username should match.");
        Assertions.assertNotEquals("password123", savedUser.getPassword(), "The password should be encoded.");
    }

    /**
     * Additional Test 1:
     * This test ensures that the loginUser method returns 'true' for a registered user with the correct password.
     */
    @Test
    void loginUser_ShouldReturnTrue_ForValidCredentials() {

        User userToRegister = new User();
        String rawPassword = "correctPassword";
        userToRegister.setUsername("login_success_user");
        userToRegister.setPassword(rawPassword);
        userService.registerUser(userToRegister);

        boolean isLoggedIn = userService.loginUser("login_success_user", rawPassword);

        Assertions.assertTrue(isLoggedIn, "Login should return true for valid credentials.");
    }

    /**
     * Additional Test 2:
     * This test ensures that the service logic correctly prevents a user from registering with a username that is already taken, by throwing a RuntimeException.
     */
    @Test
    void registerUser_ShouldThrowException_ForDuplicateUsername() {

        User firstUser = new User();
        firstUser.setUsername("duplicate_user");
        firstUser.setPassword("password_abc");
        userService.registerUser(firstUser);

        User secondUser = new User();
        secondUser.setUsername("duplicate_user");
        secondUser.setPassword("password_def");

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userService.registerUser(secondUser);
        }, "Registering a duplicate user should throw a RuntimeException.");

        Assertions.assertEquals("Username already exists", exception.getMessage());
    }
}