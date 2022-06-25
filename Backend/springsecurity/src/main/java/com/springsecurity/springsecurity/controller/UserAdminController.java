package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.entity.UserDetails;
import com.springsecurity.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserAdminController {

    @Autowired
    private UserService userService;




    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDetails> getUser(@RequestHeader("authorization") String accessToken,
                                               @PathVariable("id") final String userUuid) {
        final UserDetails User = userService.getUser(userUuid);
        return ResponseEntity.ok(User);
    }


    //create a post method named createUser with return type as ResponseEntity
    @PostMapping("/register")
    //define the method parameter user of type User. Set it final. Use @RequestBody for mapping.
    public ResponseEntity createUser(@RequestBody final UserDetails user)  {

        //register the user
        userService.register(user);
        //return http response with status set to OK
        return new ResponseEntity(HttpStatus.OK);
    }





}

