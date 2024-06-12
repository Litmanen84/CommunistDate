package com.example.CommunistDate.Users;

import java.util.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository repository;
  private final UserService service;

  @Autowired
  UserController(UserRepository repository, UserService service) {
    this.repository = repository;
    this.service = service;
  } 

  // @PreAuthorize("hasRole('ROLE_ADMIN')")
  // @GetMapping("/all")
  // public List<User> getAllUsers() {
  //   return repository.findAll();
  // }

  @PostMapping("/register")
  public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult result) {
    if (result.hasErrors()) {
      FieldError fieldError = result.getFieldError();
      if (fieldError != null) {
        System.out.println("Validation error on field " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + " " + fieldError.getCode());
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
    }

    try {
        System.out.println("No validation errors, proceeding with user creation");
        return ResponseEntity.ok(service.registerUser(registerRequest, result));
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
}

    // @PostMapping("/login")
    //   public ResponseEntity<Object> loginUser(@Valid @RequestBody LoginRequest loginRequest,
    //       BindingResult result) {
    //       try {
    //           return ResponseEntity.ok(service.loginUser(loginRequest, result));
    //       } catch (Exception e) {
    //           throw new ResponseStatusException(
    //               HttpStatus.UNAUTHORIZED, e.getMessage(), e);
    //       }
    //   }
}