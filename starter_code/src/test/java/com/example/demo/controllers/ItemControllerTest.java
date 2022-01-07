package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void find_items_happy_path(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(1l, "item1", new BigDecimal(2.45),  "description1"));
        list.add(new Item(2l, "item2", new BigDecimal(1.34),  "description2"));
        when(itemRepository.findAll()).thenReturn(list);

        final ResponseEntity response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<Item> listRetrieved = (List<Item>) response.getBody();
        Assert.assertNotNull(listRetrieved);
        Assert.assertEquals("item2", listRetrieved.get(1).getName());
    }

    @Test
    public void find_by_item_name_happy_path(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(1l, "item", new BigDecimal(2.45),  "description1"));
        list.add(new Item(2l, "item", new BigDecimal(1.34),  "description2"));
        when(itemRepository.findByName("item")).thenReturn(list);

        final ResponseEntity response = itemController.getItemsByName("item");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<Item> listRetrieved = (List<Item>) response.getBody();
        Assert.assertNotNull(listRetrieved);
        Assert.assertEquals(2, listRetrieved.size());
    }

    @Test
    public void find_items_by_id_happy_path(){
        Item item = new Item(1l, "item", new BigDecimal(2.45),  "description1");
        when(itemRepository.findById(1l)).thenReturn(java.util.Optional.of(item));
        final ResponseEntity response = itemController.getItemById(1l);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Item itemRetrivied = (Item) response.getBody();
        Assert.assertNotNull(itemRetrivied);
        Assert.assertEquals("item", itemRetrivied.getName());
    }
}
