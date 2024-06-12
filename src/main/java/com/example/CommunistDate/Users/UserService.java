package com.example.CommunistDate.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class UserService implements UserDetailsService {
    
    private final UserRepository repository;
    private final PasswordEncoder crypt;
    private final JwtService JwtService;
    // private final AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    public UserService(UserRepository repository, AuthenticationManager authenticationManager,
                       PasswordEncoder crypt, JwtService jwtService) {
        this.repository = repository;
        // this.authenticationManager = authenticationManager;
        this.crypt = crypt;
        this.JwtService = jwtService;
    }

    public Map<String, Object> registerUser(RegisterRequest registerRequest, BindingResult result) {
        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for(int i = 0; i < errorsList.size(); i++) {
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
                System.out.println("Validation error on field " + error.getField() + ": " + error.getDefaultMessage());
            }
            throw new IllegalArgumentException(errorsMap.toString());
        }

        // String password = registerRequest.getPassword();
        // if (password == null || password.isEmpty()) {
        //     throw new IllegalArgumentException("La password non può essere vuota");
        // }

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

        var otherUserByUsername = repository.findByUsername(registerRequest.getUsername());
        var otherUserByEmail = repository.findByEmail(registerRequest.getEmail());

        if (otherUserByUsername.isPresent()) {
            System.out.println("Username " + registerRequest.getUsername() + " already exists");
            throw new IllegalArgumentException("Username already exists");  
        } else if (otherUserByEmail.isPresent()) {
            System.out.println("Email " + registerRequest.getEmail() + " already exists");
            throw new IllegalArgumentException("Email already exists");
        }

        repository.save(user);
        System.out.println("User " + user.getUsername() + " registered successfully");

        String jwtToken = JwtService.createJwtToken(user);

        var response = new HashMap<String, Object>();
        response.put("user", user);
        response.put("token", jwtToken);

        return response;
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
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = repository.findByUsername(username);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
