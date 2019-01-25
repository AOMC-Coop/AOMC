package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/listUsers", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("getAllUsers");


        List<User> users;

        try {
            users = userService.getAllUsers();
        }catch (Exception e) {
            users = null;
        }



        if (users != null) {
            for(User user : users) {
                System.out.println(user.getName());
            }
            return new ResponseEntity<List<User>>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<User>>(users, HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @CrossOrigin
    public ResponseEntity<Void> removeUser(@PathVariable(value = "userId") String userId) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		String username = authentication.getName();

//		User user = userService.getUserByUserName(username);
//		Cart cart = user.getCart();
//
//		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
//		cartItemService.removeCartItem(cartItem);

        // Product product = productService.getProductById(productId);
        // product.setUnitInStock(product.getUnitInStock() + cartItem.getQuantity());
        // productService.updateProduct(product);

        if(userService.deleteUser(userId)) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }



    }
}
