package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_happy_path(){
        User fakeUser = new User();
        fakeUser.setUsername("test");
        Cart fakeCart = new Cart();
        fakeCart.setId(1l);
        fakeCart.setUser(fakeUser);
        fakeUser.setCart(fakeCart);
        when(userRepository.findByUsername("test")).thenReturn(fakeUser);

        Item fakeItem = new Item(1l, "item1", new BigDecimal(2.45),  "description1");
        when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(fakeItem));
        when(cartRepository.save(fakeCart)).thenReturn(fakeCart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1l);
        modifyCartRequest.setUsername("test");

        final ResponseEntity responseFind = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(200, responseFind.getStatusCodeValue());

        Cart cart = (Cart) responseFind.getBody();
        Assert.assertNotNull(cart);
        Assert.assertEquals(new BigDecimal(2.45), cart.getTotal());
    }

    @Test
    public void remove_from_cart_unhappy_path(){
        User fakeUser = new User();
        fakeUser.setUsername("test");
        Cart fakeCart = new Cart();
        fakeCart.setId(1l);
        fakeCart.setUser(fakeUser);
        fakeUser.setCart(fakeCart);
        when(userRepository.findByUsername("test")).thenReturn(fakeUser);

        Item fakeItem = new Item(1l, "item1", new BigDecimal(2.45),  "description1");
        when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(fakeItem));
        when(cartRepository.save(fakeCart)).thenReturn(fakeCart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1l);
        modifyCartRequest.setUsername("test");

        final ResponseEntity responseFind = cartController.removeFromcart(modifyCartRequest);
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(200, responseFind.getStatusCodeValue());

        Cart cart = (Cart) responseFind.getBody();
        Assert.assertNotNull(cart);
        Assert.assertEquals(new BigDecimal(-2.45), cart.getTotal());
    }
}
