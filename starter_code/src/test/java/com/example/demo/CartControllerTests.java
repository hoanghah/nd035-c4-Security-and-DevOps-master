package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
//        cartController = new CartController();
//        TestUtils.injectObjects(cartController, "userRepository", userRepository);
//        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
//        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    public static User createUserTest(Long id, String username){
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("password");
        user.setCart(new Cart());
        return user;
    }

    @Test
    public void test_add_card_by_user_successfully() {
        User userMock = createUserTest(1L, "testUser");
        when(userRepository.findByUsername(userMock.getUsername())).thenReturn(userMock);
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername(userMock.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void test_add_card_with_not_user_found(){
        User userMock = createUserTest(1L, "testUser");
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername("user_not_found");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void test_add_card_with_item_not_found() {
        User userMock = createUserTest(1L, "testUser");
        when(userRepository.findByUsername(userMock.getUsername())).thenReturn(userMock);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername(userMock.getUsername());
        when(itemRepository.findById(userMock.getId())).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void test_remove_cart_successfully(){
        User userMock = createUserTest(1L, "test");

        when(userRepository.findByUsername(userMock.getUsername())).thenReturn(userMock);
        when(cartRepository.save(any())).thenReturn(new Cart());
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername(userMock.getUsername());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void test_remove_cart_with_user_not_found(){
        ResponseEntity<Cart> response = cartController.removeFromcart(new ModifyCartRequest());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void test_remove_cart_with_item_not_found(){

        User userMock = createUserTest(1L, "test");

        when(userRepository.findByUsername(userMock.getUsername())).thenReturn(userMock);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setUsername(userMock.getUsername());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
    }
}
