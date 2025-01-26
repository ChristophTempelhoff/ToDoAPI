package de.tempelhoff.ToDoAPI.controller;

import de.tempelhoff.ToDoAPI.model.ToDo;
import de.tempelhoff.ToDoAPI.model.User;
import de.tempelhoff.ToDoAPI.repository.ToDoRepository;
import de.tempelhoff.ToDoAPI.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class ToDoController {

    private final ToDoRepository repository;
    private final Logger log = LoggerFactory.getLogger(ToDoController.class);

    @Autowired
    private AuthService authService = new AuthService();

    public ToDoController(ToDoRepository repo){
        this.repository = repo;
    }

    @PostMapping()
    public ResponseEntity<ToDo> create(@RequestBody ToDo toDo, @RequestHeader("api-secret") String secret){
        User user = authService.authorize(secret).getBody();
        if(user == null)return null;
        toDo.setUserID(user.getId());
        ToDo todo = repository.save(toDo);
        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Iterable<ToDo>> get(@RequestParam(name = "id", required = false, defaultValue = "0") int id, @RequestHeader("api-secret") String secret){
        User user = authService.authorize(secret).getBody();

        if(user == null) return null;

        List<ToDo> toDos = repository.findAllByUserID(user.getId());

        if(id == 0) return new ResponseEntity<>(toDos, HttpStatus.OK);
        boolean found = false;
        for(ToDo todo: toDos){
            if (todo.getToDoID().equals(id)) {
                found = true;
                break;
            }
        }
        if(!found) return new ResponseEntity("No Todo found with ID: " + id, HttpStatus.NOT_FOUND);
        toDos = new ArrayList<>();
        toDos.add(repository.findById(id).orElse(null));
        return new ResponseEntity<>(toDos, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<ToDo> update(@RequestBody ToDo other, @RequestHeader("api-secret") String secret){
        User user = authService.authorize(secret).getBody();
        if(user == null) return null;
        if(!repository.existsById(other.getToDoID())) return new ResponseEntity("No todo found with ID: " + other.getToDoID(), HttpStatus.NOT_FOUND);

        ToDo edited = repository.save(other);

        return new ResponseEntity<>(edited, HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<ToDo> updateIsDone(@RequestParam(name = "id") int id, @RequestHeader("api-secret") String secret){
        User user = authService.authorize(secret).getBody();
        if(user == null) return null;

        if(!repository.existsById(id)) return new ResponseEntity("No todo found with ID: " + id, HttpStatus.NOT_FOUND);

        ToDo todo = repository.findById(id).orElse(null);
        todo.setDone(!todo.isDone());
        todo = repository.save(todo);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam(name = "id") int id, @RequestHeader("api-secret") String secret){
        User user = authService.authorize(secret).getBody();

        if(user == null) return null;
        if(!repository.existsById(id)) return new ResponseEntity<>("No todo found with ID: " + id, HttpStatus.NOT_FOUND);

        repository.deleteById(id);
        return new ResponseEntity<String>("Successfully deleted Todo with ID: " + id, HttpStatus.OK);
    }
}
