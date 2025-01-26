package de.tempelhoff.ToDoAPI.service;

import de.tempelhoff.ToDoAPI.model.User;
import de.tempelhoff.ToDoAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("AuthService")
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<User> authorize(String secret){
        User user = userRepository.findBySecret(secret).orElse(null);
        if(user == null){
            return new ResponseEntity("Invalid API-secret", HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }
}
