package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_order_by_username_happy_path(){
        User fakeUser = new User();
        fakeUser.setUsername("test");

        List<Item> fakeListItems = new ArrayList<>();
        fakeListItems.add(new Item(1l, "item1", new BigDecimal(2.45),  "description1"));
        fakeListItems.add(new Item(2l, "item2", new BigDecimal(1.34),  "description2"));

        Cart fakeCart = new Cart();
        fakeCart.setId(1l);
        fakeCart.setUser(fakeUser);
        fakeCart.setItems(fakeListItems);
        fakeCart.setTotal(new BigDecimal(2.45+1.34));

        fakeUser.setCart(fakeCart);
        when(userRepository.findByUsername("test")).thenReturn(fakeUser);
        UserOrder fakeUserOrder = UserOrder.createFromCart(fakeUser.getCart());
        when(orderRepository.save(fakeUserOrder)).thenReturn(fakeUserOrder);

        final ResponseEntity responseFind = orderController.submit("test");
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(200, responseFind.getStatusCodeValue());

        UserOrder userOrder = (UserOrder) responseFind.getBody();
        Assert.assertNotNull(userOrder);
        Assert.assertEquals(new BigDecimal(2.45+1.34), userOrder.getTotal());

    }

    @Test
    public void get_orders_for_user_unhappy_path(){
        final ResponseEntity responseFind = orderController.getOrdersForUser("test");
        Assert.assertNotNull(responseFind);
        Assert.assertEquals(404, responseFind.getStatusCodeValue());
    }

}
