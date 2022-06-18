package com.pryabykh.userservice.controllers;

import com.pryabykh.userservice.dtos.CreateUserDto;
import com.pryabykh.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.register(createUserDto));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }
}