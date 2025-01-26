package de.tempelhoff.ToDoAPI.controller;

import de.tempelhoff.ToDoAPI.model.User;
import de.tempelhoff.ToDoAPI.repository.UserRepository;
import de.tempelhoff.ToDoAPI.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://127.0.0.1:4200")
public class UserController {

    @Autowired
    private AuthService authService;

    private final UserRepository repository;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository repo){
        this.repository = repo;
    }

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user){
        try {
            user.setSecret(UUID.randomUUID().toString());
            user = repository.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch(Exception ex){
            if(ex.getMessage().contains("Duplicate entry")){
              return new ResponseEntity("A user with this Email already exists", HttpStatus.BAD_REQUEST);
            } else {
                log.error(ex.toString());
                return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password){
        User user = repository.findByEmailAndPassword(email, password).orElse(null);

        if(user == null) return new ResponseEntity<>("Email or password wrong", HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>("API-Secret: " + user.getSecret(), HttpStatus.OK);

    }

    @GetMapping()
    public ResponseEntity<User> get(@RequestHeader("api-secret") String secret){
        ResponseEntity<User> response = authService.authorize(secret);
        User user = response.getBody();
        if(user == null) return new ResponseEntity(response.getStatusCode());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
