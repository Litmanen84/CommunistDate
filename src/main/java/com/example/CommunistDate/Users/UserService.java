package com.example.CommunistDate.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import com.example.CommunistDate.config.ErrorResponse;
import com.example.CommunistDate.config.FieldErrorResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class UserService implements UserDetailsService {
    
    private final UserRepository repository;
    private final PasswordEncoder crypt;
    private final JwtService JwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    @Lazy
    public UserService(UserRepository repository, AuthenticationManager authenticationManager,
                       PasswordEncoder crypt, JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.crypt = crypt;
        this.JwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public Map<String, Object> registerUser(RegisterRequest registerRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldErrorResponse> fieldErrors = result.getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    
            ErrorResponse errorResponse = new ErrorResponse("Validation failed", fieldErrors);
            throw new IllegalArgumentException(errorResponse.toString());
        }
    
        Optional<User> userByUsername = repository.findByUsername(registerRequest.getUsername());
        Optional<User> userByEmail = repository.findByEmail(registerRequest.getEmail());
    
        if (userByUsername.isPresent() || userByEmail.isPresent()) {
            List<FieldErrorResponse> fieldErrors = new ArrayList<>();
            if (userByUsername.isPresent()) {
                fieldErrors.add(new FieldErrorResponse("username", "Username already exists"));
            }
            if (userByEmail.isPresent()) {
                fieldErrors.add(new FieldErrorResponse("email", "Email already exists"));
            }
            ErrorResponse errorResponse = new ErrorResponse("Validation failed", fieldErrors);
            throw new IllegalArgumentException(errorResponse.toString());
        }
    
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(crypt.encode(registerRequest.getPassword()));
        user.setGender(registerRequest.getGender());
        user.setCity(registerRequest.getCity());
        user.setNationality(registerRequest.getNationality());
        user.setAge(registerRequest.getAge());
        user.setPartnerShare(registerRequest.getPartnerShare());
        user.setCountryOfResidence(registerRequest.getCountryOfResidence());
        user.setLanguage(registerRequest.getLanguage());
        user.setPoliticalBelief(registerRequest.getPoliticalBelief());
        user.setIsAdmin(false);
        user.setCommunismLevel(registerRequest.getCommunismLevel().orElse(0));
    
        repository.save(user);
        String jwtToken = JwtService.createJwtToken(user);
    
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", jwtToken);
    
        return response;
    }

    public Map<String, Object> loginUser(LoginRequest loginRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldErrorResponse> fieldErrors = result.getFieldErrors().stream()
                .map(fieldError -> new FieldErrorResponse(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    
            ErrorResponse errorResponse = new ErrorResponse("Validation failed", fieldErrors);
            throw new IllegalArgumentException(errorResponse.toString());
        }
            try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), 
                loginRequest.getPassword())
            );
            User user = repository.findByUsername(loginRequest.getUsername())
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
            String jwtToken = JwtService.createJwtToken(user);
    
            var response = new HashMap<String, Object>();
            response.put("user", user);
            response.put("token", jwtToken);
            return response;
            }
            
            catch (Exception e) {
            System.out.println("There is an Exception:");
            e.printStackTrace();
            }
            
            return null;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDetailsService.loadUserByUsername(username);
    }
}
