package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity response = userController.createUser(createUserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User user = (User) response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0, user.getId());
        Assert.assertEquals("test", user.getUsername());
        Assert.assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void find_user_by_username_happy_path(){
        User fakeUser = new User();
        fakeUser.setUsername("test");
        when(userRepository.findByUsername("test")).thenReturn(fakeUser);

        final ResponseEntity responseFind = userController.findByUserName("test");
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(200, responseFind.getStatusCodeValue());

        User user = (User) responseFind.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("test", user.getUsername());
    }

    @Test
    public void find_user_by_username_unhappy_path(){

        final ResponseEntity responseFind = userController.findByUserName("test");
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(404, responseFind.getStatusCodeValue());

    }

    @Test
    public void find_user_by_id_happy_path(){
        User fakeUser = new User();
        fakeUser.setId(1);
        fakeUser.setUsername(("test"));
        when(userRepository.findById(1l)).thenReturn(java.util.Optional.of(fakeUser));

        final ResponseEntity responseFind = userController.findById(1l);
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(200, responseFind.getStatusCodeValue());

        User user = (User) responseFind.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("test", user.getUsername());
        Assert.assertEquals(1, user.getId());
    }

}
